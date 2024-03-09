package ru.antonmarin.autoget.actions

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.framework.DataFactory

class ListTest {
    @Test
    fun `should return list of entries when created from list`() {
        val expectedEntries = listOf(DataFactory.entry())
        val action = List(expectedEntries)

        Assertions.assertThat(action.execute(emptyList())).isEqualTo(expectedEntries)
    }
}
