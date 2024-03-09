package ru.antonmarin.autoget.server.configuration

import ru.antonmarin.autoget.Action
import ru.antonmarin.autoget.Pipeline
import ru.antonmarin.autoget.actions.RssItems
import ru.antonmarin.autoget.actions.contracts.XmlReader
import ru.antonmarin.autoget.server.Controller
import ru.antonmarin.autoget.server.Server
import ru.antonmarin.autoget.server.ServerItem
import ru.antonmarin.autoget.server.controller.OnceController
import ru.antonmarin.autoget.server.controller.TimerController
import java.net.URL
import java.time.Clock
import java.time.Duration
import kotlin.reflect.KProperty1

class ConfigServerFactory(clock: Clock) {
    private val itemFactory = ConfigServerItemFactory(ConfigControllerFactory(clock))

    fun create(config: ServerConfig): Server {
        return Server(
            itemFactory.create(config.items)
        )
    }
}
data class ServerConfig(
    val items: List<ServerItemConfig>
)

class ConfigServerItemFactory(
    private val controllerFactory: ConfigControllerFactory,
) {
    fun create(itemsConfig: List<ServerItemConfig>): List<ServerItem> {
        return itemsConfig.map { config ->
            ServerItem(
                controller = controllerFactory.create(config.`when`),
                pipeline = Pipeline(emptyList()),
            )
        }
    }
}
data class ServerItemConfig(
    val `when`: WhenConfig,
    val pipeline: List<ActionConfig<out Action>>,
)

class ConfigControllerFactory(private val clock: Clock) {
    fun create(config: WhenConfig): Controller {
        return when (config.`when`) {
            WhenConfig.WhenEnum.ONCE -> OnceController()
            WhenConfig.WhenEnum.EVERY -> TimerController(
                delayFromStartMilliseconds = config.values[WhenConfig.ValuesKeys.DELAY_FROM_START.key]
                    ?.let { Duration.parse(it).toMillis() }
                    ?: 0L,
                delayBetweenSuccessive = config.values[WhenConfig.ValuesKeys.DELAY_BETWEEN_SUCCESSIVE.key]
                    ?.let { Duration.parse(it) }
                    ?: Duration.ofHours(1),
                clock = clock,
            )
        }
    }
}
data class WhenConfig(
    val `when`: WhenEnum,
    val values: Map<String, String> = emptyMap(),
) {
    enum class WhenEnum {
        ONCE,
        EVERY,
    }

    enum class ValuesKeys(val key: String) {
        DELAY_FROM_START("delayFromStart"),
        DELAY_BETWEEN_SUCCESSIVE("delayBetweenSuccessive"),
    }
}


class ConfigActionFactory(
    private val xmlReader: XmlReader,
) {
    fun create(config: ActionConfig<out Action>): Action {
//        config.values.keys.first().
        return RssItems(xmlReader, URL(config.values[RssItems::url]))
    }
}
class ActionConfig<T>(val values: Map<KProperty1<T, Any>, String> = emptyMap())
