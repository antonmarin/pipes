package ru.antonmarin.autoget.server.controller

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.Pipeline

class OnceControllerTest {
    private val pipeline = mockk<Pipeline>()
    private val controller = OnceController()

    @Test
    fun `should execute pipeline once`() {
        var count = 0
        every { pipeline.execute() } answers { count++ }

        controller.execute(pipeline)

        Assertions.assertThat(count).isEqualTo(1)
    }
}
