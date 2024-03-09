package ru.antonmarin.autoget.infra

import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DIContainerTest {
    private val container = DIContainer()

    @Test
    fun `should return when registered by type`() {
        val expectedObject = mockk<TestClass>()
        container.register(TestClass::class, expectedObject)

        val value = container.getObject<TestClass>(TestClass::class)

        Assertions.assertThat(value).isEqualTo(expectedObject)
    }

    @Test
    fun `should exception when not registered`() {
        Assertions.assertThatThrownBy {
            container.getObject<TestClass>(TestClass::class)
        }.isInstanceOf(NotRegisteredException::class.java)
    }

    class TestClass
}
