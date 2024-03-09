package ru.antonmarin.autoget.server.controller

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.Pipeline
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class TimerControllerTest {
    private val pipeline = mockk<Pipeline>()
    private val clock = Clock.fixed(Instant.parse("2023-10-23T14:00:10Z"), ZoneId.systemDefault())

    @Volatile
    var count = 0

    @Test
    fun `should execute pipeline not once`() {
        every { pipeline.execute() } answers { count++ }

        val controller = TimerController(0, Duration.ofMillis(10), clock)
        controller.execute(pipeline)
        Thread.sleep(30)
        controller.stop()

        Assertions.assertThat(count).isGreaterThan(1)
    }
}
