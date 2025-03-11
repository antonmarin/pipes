package ru.antonmarin.autoget

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.framework.DataFactory

class PipelineTest {
    @Nested
    inner class Execute {
        private val capturedAction1Input = slot<List<Entry>>()
        private val capturedAction2Input = slot<List<Entry>>()
        private val inputEntries = listOf(DataFactory.entry())
        private val input = mockk<Action> {
            every { execute(any()) } answers { inputEntries }
        }

        @Test
        fun `should pass entries list through all actions when all actions returns input`() {
            val action1 = mockk<Action>()
            every { action1.execute(capture(capturedAction1Input)) } answers { firstArg() }
            val action2 = mockk<Action>()
            every { action2.execute(capture(capturedAction2Input)) } answers { firstArg() }


            val pipeline = Pipeline(listOf(input, action1, action2))
            pipeline.execute()

            Assertions.assertThat(capturedAction1Input.captured).isEqualTo(inputEntries)
            Assertions.assertThat(capturedAction2Input.captured).isEqualTo(inputEntries)
        }

        @Test
        fun `should pass entries returned from previous action when action returned different`() {
            val action1 = mockk<Action>()
            val a1output = emptyList<Entry>()
            every { action1.execute(capture(capturedAction1Input)) } answers { a1output }
            val action2 = mockk<Action>()
            every { action2.execute(capture(capturedAction2Input)) } answers { firstArg() }

            val pipeline = Pipeline(listOf(input, action1, action2))
            pipeline.execute()

            Assertions.assertThat(capturedAction1Input.captured).isEqualTo(inputEntries)
            Assertions.assertThat(capturedAction2Input.captured).isEqualTo(a1output)
        }

        @Test
        fun `should not fail when action exception`() {
            val actionThrowsException = mockk<Action>()
            every { actionThrowsException.execute(any()) } throws RuntimeException("Should not be thrown")

            val pipeline = Pipeline(listOf(actionThrowsException))
            Assertions.assertThatCode {
                pipeline.execute()
            }.doesNotThrowAnyException()
        }
    }
}
