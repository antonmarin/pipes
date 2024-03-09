package ru.antonmarin.autoget.server

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ServerTest {
    @Test
    fun `should execute all controllers when start`() {
        var c1count = 0
        val controller1 = mockk<Controller> {
            every { execute(any()) } answers { c1count++;Unit }
        }
        var c2count = 0
        val controller2 = mockk<Controller> {
            every { execute(any()) } answers { c2count++;Unit }
        }

        Server(listOf(ServerItem(controller1, mockk()), ServerItem(controller2, mockk()))).start()

        Assertions.assertThat(c1count).isEqualTo(1)
        Assertions.assertThat(c2count).isEqualTo(1)
    }
}
