package ru.antonmarin.autoget

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TemplateTest {
    @Test
    fun `should replace quoted token with value`() {
        val template = Template("some {title} is title")

        val result = template.replace(mapOf("title" to "ReplacedTitle"))

        Assertions.assertThat(result).isEqualTo("some ReplacedTitle is title")
    }
}
