package ru.antonmarin.autoget.server.configuration

import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.Pipeline

class ConfigServerFactoryTest {
    private val factory = ConfigServerFactory(mockk())

    @Disabled("Until actions factory")
    @Test
    fun `should create serverItem for each item in config file`() {
        val config = ServerConfig(
            listOf(
                ServerItemConfig(
                    mockk(),
                    emptyList()
                )
            )
        )

        val server = factory.create(config)

        Assertions.assertThat(server.items.size).isEqualTo(1)
        val item = server.items.first()
        Assertions.assertThat(item.pipeline).isInstanceOf(Pipeline::class.java)
    }
}
