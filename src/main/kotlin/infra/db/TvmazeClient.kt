package ru.antonmarin.autoget.infra.db

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.actions.AkasProvider
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import kotlin.reflect.KClass


class TvmazeClient(@Suppress("unused") private val httpClient: HttpClient) : AkasProvider {
    private val jsonMapper = JsonMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    override fun getAkas(input: String): List<String> {
        val inputUrl = urlEncode(input)
        val res = singleSearchResponse(inputUrl, SingleSearchResponse::class)
        if (res == null) logger.trace("$input not found in database")

        return res?.embedded?.akas?.map { it.name } ?: emptyList()
    }

    private fun urlEncode(string: String): String = URLEncoder.encode(string, StandardCharsets.UTF_8)

    private fun <T : Any> singleSearchResponse(
        inputUrl: String,
        type: KClass<T>,
    ): T? {
        val request = HttpRequest.newBuilder()
            .uri(URI("$TVMAZE_BASE/singlesearch/shows?q=$inputUrl&embed=akas"))
            .build()
        logger.trace("Sending request {}", request)
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        logger.trace("Received response {} with body\n{}", response, response.body())
        return jsonMapper.readValue(response.body(), type.java)
    }

    data class SingleSearchResponse(
        @JsonProperty("_embedded")
        val embedded: Embedded,
    )

    data class Embedded(
        val akas: List<Aka>,
    )

    data class Aka(
        val name: String,
    )

    companion object {
        private val logger = LoggerFactory.getLogger(TvmazeClient::class.java)
        private const val TVMAZE_BASE = "https://api.tvmaze.com"
    }
}
