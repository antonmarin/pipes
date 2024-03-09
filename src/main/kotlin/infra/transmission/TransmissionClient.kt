package ru.antonmarin.autoget.infra.transmission

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.actions.contracts.Torrent
import ru.antonmarin.autoget.actions.contracts.TorrentClient
import ru.antonmarin.autoget.actions.contracts.TorrentFile
import ru.antonmarin.autoget.domain.Either
import ru.antonmarin.autoget.infra.transmission.requests.TorrentAddRequest
import ru.antonmarin.autoget.infra.transmission.requests.TorrentGetRequest
import ru.antonmarin.autoget.infra.transmission.requests.TorrentRenamePathRequest
import ru.antonmarin.autoget.infra.transmission.requests.TorrentResponse
import java.io.IOException
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.reflect.KClass

/**
 * https://github.com/transmission/transmission/blob/main/docs/rpc-spec.md
 */
class TransmissionClient(
    private val apiURL: URL,
    private val httpClient: HttpClient = HttpClient.newBuilder().build(),
) : TorrentClient {
    private val jsonMapper = JsonMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }
    private val sessionId: String
        get() {
            val request = HttpRequest.newBuilder()
                .uri(apiURL.toURI())
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build()
            return try {
                val response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString()
                )
                val sessionId = response.headers().firstValue(HEADER_SESSION_ID).get()
                logger.trace("Received new sessionId {}", sessionId)
                sessionId
            } catch (e: IOException) {
                logger.error("Error receiving sessionId", e)
                throw e
            }
        }

    private fun <ResponseClass : Any> request(requestArguments: TransmissionRequest<ResponseClass>): Either<RequestError, ResponseClass> {
        val requestTag = Random.nextInt().absoluteValue
        val bodyContent = jsonMapper.writeValueAsString(
            Request(
                method = requestArguments.provideMethodName(),
                arguments = requestArguments,
                tag = requestTag,
            )
        )
        val request = HttpRequest.newBuilder()
            .uri(apiURL.toURI())
            .setHeader(HEADER_SESSION_ID, sessionId)
            .POST(HttpRequest.BodyPublishers.ofString(bodyContent))
            .build()

        logger.trace("Sending request {} with headers {}, body\n{}", request, request.headers(), bodyContent)
        val response = try {
            httpClient.send(
                request, HttpResponse.BodyHandlers.ofString()
            )
        } catch (e: IOException) {
            logger.error("Request failed", e)
            return Either.Left(RequestError.REQUEST_FAILED)
        } catch (e: InterruptedException) {
            logger.debug("Request $request interrupted", e)
            return Either.Left(RequestError.REQUEST_INTERRUPTED)
        }
        logger.trace("Received response {} with headers {}, body\n{}", response, response.headers(), response.body())

        val body = response.body()
        val res = Either
            .catch { jsonMapper.readValue(body, Response::class.java) }
            .mapLeft {
                logger.error("Received unexpected response $body", it)
                RequestError.UNEXPECTED_RESPONSE
            }.let {
                when (it) {
                    is Either.Left -> return it
                    is Either.Right -> it.value
                }
            }
        if (res.result != SUCCESS_RESULT) {
            logger.error("Received ${requestArguments.provideMethodName()} error response: ${res.result} with body ${response.body()}")
            return Either.Left(RequestError.ERROR_RESPONSE)
        }
        if (res.tag != requestTag) {
            logger.error("Received response with different tag")
        }

        return Either.catch {
            jsonMapper.readValue(jsonMapper.writeValueAsString(res.arguments), requestArguments.responseClass().java)
        }.mapLeft {
            logger.error(
                "Failed mapping response arguments ${res.arguments} to class ${requestArguments.responseClass()}",
                it,
            )
            RequestError.RESPONSE_MAPPING
        }
    }

    enum class RequestError {
        UNEXPECTED_RESPONSE,
        ERROR_RESPONSE,
        RESPONSE_MAPPING,
        REQUEST_FAILED,
        REQUEST_INTERRUPTED,
    }

    interface TransmissionRequest<ResponseClass : Any> {
        fun provideMethodName(): String
        fun responseClass(): KClass<ResponseClass>
    }

    /**
     * {
     *    "arguments": {
     *      "fields": [
     *        "version"
     *      ]
     *    },
     *    "method": "session-get",
     *    "tag": 912313
     * }
     */
    @Suppress("unused")
    inner class Request(
        val method: String,
        val arguments: Any,
        val tag: Int,
    )

    /**
     * {
     *    "arguments": {
     *       "version": "2.93 (3c5870d4f5)"
     *    },
     *    "result": "success",
     *    "tag": 912313
     * }
     */
    class Response(
        val result: String,
        val arguments: Map<String, Any>,
        val tag: Int,
    )

    override fun rename(id: Long, from: String, to: String): Boolean =
        this.request(TorrentRenamePathRequest(id, to, from)).let { true }

    override fun findOne(id: Long): Torrent? = request(TorrentGetRequest(listOf(id)))
        .fold(
            ifLeft = { null },
            ifRight = { it.torrents.firstOrNull() },
        )?.let { response ->
            Torrent(
                response.id ?: return mappingFailed(response, "id"),
                response.name ?: return mappingFailed(response, "title"),
                response.metadataPercentComplete ?: return mappingFailed(response, "metadataReceived"),
                response.files?.map { TorrentFile(it.name) } ?: return mappingFailed(response, "files")
            )
        }

    override fun add(uri: URI, downloadDir: String?): Long {
        val addedId = request(TorrentAddRequest(uri, downloadDir))
            .fold(
                ifLeft = { null },
                ifRight = { it.torrentAdded?.id ?: it.torrentDuplicate?.id },
            )
        // todo refactor to safe calls
        return addedId ?: throw RuntimeException("Failed adding torrent")
    }

    private fun mappingFailed(response: TorrentResponse, property: String): Torrent? {
        logger.error("Failed mapping $property Torrent property from response: $response")
        return null
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TransmissionClient::class.java)

        private const val HEADER_SESSION_ID = "x-transmission-session-id"
        private const val SUCCESS_RESULT = "success"
    }
}
