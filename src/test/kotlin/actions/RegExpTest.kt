package ru.antonmarin.autoget.actions

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.framework.DataFactory
import ru.antonmarin.autoget.Entry

class RegExpTest {
    private val input = listOf(
        DataFactory.entry("some title", "Плохое описание"),
        DataFactory.entry("Магия и мускулы [WEBRip 1080p HEVC]")
    )

    @Test
    fun `should remove entries when action reject and field match any`() {
        val action = RegExp(RegExp.Action.REJECT, Entry::title, listOf(Regex("Магия и мускулы .*HEVC")))

        val result = action.execute(input)

        Assertions.assertThat(result).isEqualTo(listOf(input[0]))
    }

    @Test
    fun `should leave only entries when action accept and field match any`() {
        val action = RegExp(RegExp.Action.ACCEPT, Entry::title, listOf(Regex("Магия и мускулы .*HEVC")))

        val result = action.execute(input)

        Assertions.assertThat(result).isEqualTo(listOf(input[1]))
    }

    @Test
    fun `should remove entries by description when action reject and field desc`() {
        val action = RegExp(RegExp.Action.REJECT, Entry::description, listOf(Regex("Плохое описание")))

        val result = action.execute(input)

        Assertions.assertThat(result).isEqualTo(listOf(input[1]))
    }
}
