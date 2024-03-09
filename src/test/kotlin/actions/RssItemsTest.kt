package ru.antonmarin.autoget.actions

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.framework.DataFactory
import ru.antonmarin.autoget.actions.contracts.XmlReader

class RssItemsTest {
    private val xmlReader = mockk<XmlReader>()

    @Test
    fun `should populate entry for every rssChannelItem when received`() {
        val rss1 = DataFactory.rssItem("item1 title")
        val rss2 = DataFactory.rssItem("item2 title")
        every { xmlReader.read(any(), RssFeed::class) } returns RssFeed(RssChannel(listOf(
            rss1,
            rss2,
        )))

        val action = RssItems(xmlReader, mockk())
        val result = action.execute(emptyList())

        Assertions.assertThat(result).isEqualTo(listOf(
            DataFactory.entry(title = rss1.title, description = rss1.description),
            DataFactory.entry(title = rss2.title, description = rss2.description),
        ))
    }

    @Test
    fun `should populate torrentUri property from custom field when set`() {
        val rss1 = DataFactory.rssItem("item1 title")
        every { xmlReader.read(any(), RssFeed::class) } returns RssFeed(RssChannel(listOf(
            rss1,
        )))

        val action = RssItems(xmlReader, mockk())
        val result = action.execute(emptyList())

        Assertions.assertThat(result).isEqualTo(listOf(
            DataFactory.entry(title = rss1.title, description = rss1.description, torrentUrl = rss1.magneturi),
        ))
    }
}
