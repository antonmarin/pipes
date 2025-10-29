package ru.antonmarin.autoget.actions

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import ru.antonmarin.autoget.Action
import ru.antonmarin.autoget.Entry
import ru.antonmarin.autoget.actions.contracts.XmlReader
import java.net.URI
import java.net.URL
import kotlin.collections.List

class RssItems(
    private val reader: XmlReader,
    val url: URL,
) : Action {
    override fun execute(entries: List<Entry>): List<Entry> {
        //todo implement "get feed from since" logic
        return reader.read(url, RssFeed::class).channel.items.map {
            Entry(
                title = it.title,
                description = /*it.description*/ "",
                torrentUri = it.enclosure?.url?.toURI(), //todo how to customize source property?
            )
        }
    }
}

/**
 * @see <a href="https://www.rssboard.org/rss-specification">Specification</a>
 */
data class RssFeed(
    val channel: RssChannel
)

data class RssChannel(
    @JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(useWrapping = false)
    val items: List<RssItem>
)

/**
 * @param author email of author
 * @param guid uniquely identifies the item
 */
data class RssItem(
    val title: String,
    val link: URL?, // not everyone implement required
//    val description: String, description now used now but catch null some way
    val author: String?,
    val category: String?,
    val comments: URI?,
    val enclosure: Enclosure?,
//    val pubDate: LocalDateTime?,
    val guid: String?,
//    val contentLength
    // todo not standard rss property. implement some way of extending. mb generic of RssItems?
    val magneturi: URI?,
//    val source
)

data class Enclosure(
    val url: URL,
    val length: Long,
    val type: String, // mime type
)
/**
 * title	The title of the item.	Venice Film Festival Tries to Quit Sinking
 * link	The URL of the item.	http://nytimes.com/2004/12/07FEST.html
 * description	The item synopsis.	<description>Some of the most heated chatter at the Venice Film Festival this week was about the way that the arrival of the stars at the Palazzo del Cinema was being staged.</description>
 * author	Email address of the author of the item. More.
 * category	Includes the item in one or more categories. More.
 * comments	URL of a page for comments relating to the item. More.
 * enclosure	Describes a media object that is attached to the item. More.
 * guid	A string that uniquely identifies the item. More.
 * pubDate	Indicates when the item was published. More.
 * source	The RSS channel that the item came from. More.
 * ------
 * magneturi ??? not in spec
 * contentLength
 */
