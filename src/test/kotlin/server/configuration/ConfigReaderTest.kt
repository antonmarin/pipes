package ru.antonmarin.autoget.server.configuration

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.actions.AddTorrent
import ru.antonmarin.autoget.actions.RegExp
import ru.antonmarin.autoget.actions.RssItems

class ConfigReaderTest {
    private val reader = ConfigReader()

    @Disabled("Until factories ready")
    @Test
    fun `should read yaml config when valid`() {
        val configFile = """
            ---
            items:
            - when: once
              pipeline:
                - action: RssItems
                - action: RegExp
                - action: AddTorrent
            - when: once
              pipeline:
            - when: once
              pipeline:
        """.trimIndent().byteInputStream()

        val config = reader.read(configFile)

        Assertions.assertThat(config).isInstanceOf(ServerConfig::class.java)
        Assertions.assertThat(config.items).isEqualTo(
            listOf(
                ServerItemConfig(
                    WhenConfig(WhenConfig.WhenEnum.ONCE, mapOf("delayFromStart" to "PT1M")),
                    listOf(ActionConfig<RssItems>(), ActionConfig<RegExp>(), ActionConfig<AddTorrent>())
                ),
                ServerItemConfig(WhenConfig(WhenConfig.WhenEnum.ONCE, mapOf("delayFromStart" to "PT1M")), listOf()),
                ServerItemConfig(WhenConfig(WhenConfig.WhenEnum.ONCE, mapOf("delayFromStart" to "PT1M")), listOf()),
            )
        )
    }
}
