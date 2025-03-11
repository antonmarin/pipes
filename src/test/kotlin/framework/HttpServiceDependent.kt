package ru.antonmarin.autoget.framework

import org.junit.jupiter.api.BeforeEach
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.utility.DockerImageName

/**
 * https://java.testcontainers.org/modules/mockserver/
 */
interface HttpServiceDependent {

    fun mockResponse(path: String, responseBody: String, statusCode: Int = 200): String {
        client
            .`when`(request(path))
            .respond(
                response()
                    .withStatusCode(statusCode)
                    .withBody(responseBody)
            )

        return server.endpoint + path
    }

    @BeforeEach
    fun resetStubs() {
        client.reset()
    }

    companion object {
        private val server = MockServerContainer(
            DockerImageName
                .parse("mockserver/mockserver")
                .withTag("mockserver-" + MockServerClient::class.java.getPackage().implementationVersion)
        )
            .also { it.start() }
        private val client = MockServerClient(server.host, server.serverPort)
    }
}
