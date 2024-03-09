package ru.antonmarin.autoget.actions

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.framework.DataFactory

class InTitlesListTest {
    @Nested
    inner class InTitlesListTest {
        @Test
        fun `should filter entries when title contains`() {
            val titlesProvider = mockk<TitlesProvider>()
            every { titlesProvider.getTitles() } returns listOf("Some title")
            val entry = DataFactory.entry(title = "Some title HEVC")

            val action = InTitlesList(titlesProvider)
            val filtered = action.execute(listOf(entry))

            Assertions.assertThat(filtered).isEqualTo(listOf(entry))
        }
    }

    @Nested
    inner class AddAkasListTest {
        @Test
        fun `should add akas in list when found`() {
            val sourceTitle = "Ninja Kamui"
            val input = mockk<TitlesProvider> { every { getTitles() } returns listOf(sourceTitle) }
            val akas = listOf(
                "Ниндзя Камуи"
            )
            val akasProvider = mockk<AkasProvider> { every { getAkas(any())} returns akas }

            val list = AddAkasList(akasProvider, input)
            val titles = list.getTitles()

            Assertions.assertThat(titles).containsExactlyInAnyOrderElementsOf(listOf(sourceTitle) + akas)
        }
    }

}
