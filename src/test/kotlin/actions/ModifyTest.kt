package ru.antonmarin.autoget.actions

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.framework.DataFactory
import ru.antonmarin.autoget.Entry

class ModifyTest {
    @Nested
    inner class PropertyRegex {
        @Test
        fun `should fill videoResolution when value found`() {
            // todo add case in the end
            val sutEntry = DataFactory.entry(title = "Some title 1080p HEVC something else")

            val action = Modify(Entry::videoResolution, Entry::title, Regex("\\s(\\d+[pi])"))
            action.execute(listOf(sutEntry))

            Assertions.assertThat(sutEntry.videoResolution).isEqualTo("1080p")
        }

        @Test
        fun `should replace title when source is same property`() {
            val sutEntry = DataFactory.entry(title = "Выходные Господина Злодея / серии: 1-11 [WEBRip 1080p HEVC] / Kyuujitsu no Warumono-san")

            val action = Modify(Entry::title, Entry::title, Regex("([^/]+)\\s/"))
            action.execute(listOf(sutEntry))

            Assertions.assertThat(sutEntry.title).isEqualTo("Выходные Господина Злодея")
        }
    }

    @Nested
    inner class ModifyTemplate {
        @Test
        fun `should replace with templated values when `() {
            val sutEntry = DataFactory.entry(title = "SomeTitle", videoResolution = "1080i")

            val action = Modify(Entry::torrentTitle, "{title} [{videoResolution} some suffix]")
            action.execute(listOf(sutEntry))

            Assertions.assertThat(sutEntry.torrentTitle).isEqualTo("SomeTitle [1080i some suffix]")
        }
    }
}
