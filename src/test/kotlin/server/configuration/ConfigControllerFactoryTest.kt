package ru.antonmarin.autoget.server.configuration

import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.framework.Reflection
import ru.antonmarin.autoget.server.controller.OnceController
import ru.antonmarin.autoget.server.controller.TimerController
import java.time.Clock
import java.time.Duration

class ConfigControllerFactoryTest {
    private val clock = mockk<Clock>()
    private val factory = ConfigControllerFactory(clock)

    @Test
    fun `should create OnceController when once configured`() {
        val config = WhenConfig(WhenConfig.WhenEnum.ONCE)

        val controller = factory.create(config)

        Assertions.assertThat(controller).isInstanceOf(OnceController::class.java)
    }

    @Test
    fun `should create TimerController when every configured`() {
        val config = WhenConfig(WhenConfig.WhenEnum.EVERY, mapOf(
            WhenConfig.ValuesKeys.DELAY_FROM_START.key to "PT1M",
            WhenConfig.ValuesKeys.DELAY_BETWEEN_SUCCESSIVE.key to "PT2H",
            ))

        val controller = factory.create(config)

        Assertions.assertThat(controller).isInstanceOf(TimerController::class.java)
        Assertions
            .assertThat(Reflection.getPrivatePropertyValue<Long>(controller, "delayFromStartMilliseconds"))
            .isEqualTo(Duration.parse(config.values["delayFromStart"]!!).toMillis())
        Assertions
            .assertThat(Reflection.getPrivatePropertyValue<Duration>(controller, "delayBetweenSuccessive"))
            .isEqualTo(Duration.parse(config.values["delayBetweenSuccessive"]!!))
        Assertions
            .assertThat(Reflection.getPrivatePropertyValue<Clock>(controller, "clock"))
            .isEqualTo(clock)
    }
}
