package ru.antonmarin.autoget.infra.db

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File
import java.net.http.HttpClient

class TvmazeClientTest {
    @Nested
    inner class AkasProviderTest {
        private val httpClient = mockk<HttpClient>()
        private val provider = TvmazeClient(httpClient)

        @Test
        fun `should provide akas when response successful`() {
            val responseBody = File("src/test/resources/tvmaze-akas.json").readText()
            every { httpClient.send<String>(any(), any()) } returns mockk {
                every { body() } returns responseBody
            }

            val input = "input"
            val result = provider.getAkas(input)

            Assertions.assertThat(result).isEqualTo(
                listOf(
                    "Ниндзя Камуи",
                )
            )
        }
    }
}
