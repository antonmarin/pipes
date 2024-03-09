package ru.antonmarin.autoget.infra.xml

import com.fasterxml.jackson.databind.DatabindException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.actions.contracts.XmlReader
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.reflect.KClass

class JacksonReader(
    private val httpClient: HttpClient = HttpClient.newBuilder().build(),
) : XmlReader {
    private val mapper: ObjectMapper = XmlMapper().apply {
        registerKotlinModule()
        registerModule(JavaTimeModule())
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    @Throws(DatabindException::class)
    override fun <ResponseType : Any> read(url: URL, className: KClass<ResponseType>): ResponseType {
        logger.trace("Reading content from $url")
        val response = httpClient.send(
            HttpRequest.newBuilder()
                .uri(url.toURI())
                .GET()
                .build(), HttpResponse.BodyHandlers.ofString())
        logger.trace("Parsing content: {}", response.body())
        return try {
            mapper.readValue(response.body(), className.java)
        } catch (e: DatabindException) {
            logger.debug("Failed deserializing response {}: {}", response, response.body())
            throw e
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JacksonReader::class.java)
    }
}
