package ru.antonmarin.autoget.infra.xml

import com.fasterxml.jackson.databind.DatabindException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.framework.DataFactory
import ru.antonmarin.autoget.actions.Enclosure
import ru.antonmarin.autoget.actions.RssChannel
import ru.antonmarin.autoget.actions.RssFeed
import ru.antonmarin.autoget.framework.HttpServiceDependent
import java.net.URI
import java.net.URL

class JacksonReaderTest : HttpServiceDependent {
    private val reader = JacksonReader()

    @Nested
    inner class RssTest {
        @Test
        fun `should deserialize to requested class when responded`() {
            val absUrl = mockResponse("/rss", "<xml><title>response</title></xml>")

            val response = reader.read(URL(absUrl), TestResponse::class)

            Assertions.assertThat(response).isEqualTo(TestResponse("response"))
        }

        @Test
        fun `should error when failed deserializing response`() {
            val absUrl = mockResponse("/rss", "<xml>avadakedavra</xml>")


            Assertions.assertThatThrownBy {
                reader.read(URL(absUrl), TestResponse::class)
            }.isInstanceOf(DatabindException::class.java)
        }

        @Test
        fun `should deserialize when response is non standard rss`() {
            val absUrl = mockResponse("/rss", RSS_NON_STANDARD)

            val response = reader.read(URL(absUrl), RssFeed::class)

            Assertions.assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(
                    RssFeed(
                        RssChannel(
                            listOf(
                                DataFactory.rssItem(
                                    title = "Принц демонов дома Момочи / серии: 1-10 [WEBRip 1080p] / Momochi-san Chi no Ayakashi Ouji",
                                    link = URL("https://tv3.someurl.it/release/momochi-san-chi-no-ayakashi-ouji"),
                                    description = "В день своего шестнадцатилетия сирота Химари Момочи получает прекрасное поместье своих покойных родителей. Для девушки, которая выросла в приюте, это стало замечательной новостью, ведь у неё наконец-то появился собственный уютный уголок, где можно чувствовать себя как дома.\n" +
                                            "Но если бы всё было так просто! На самом деле, это поместье существует на границе между человеческим и духовным мирами, а сама девушка, будучи его хозяйкой, призвана выступать в качестве хранителя этой границы. Но и это ещё не всё! Как оказалось, в её будущем жилище незаконно поселились трое парней-красавчиков, и теперь они заявляют, что это их дом, а ей лучше бы убираться оттуда подобру-поздорову!<br><br>",
                                    enclosure = Enclosure(
                                        url = URL("https://redirect.asdf.it/upload/torrents/27892.torrent"),
                                        length = 15057857801,
                                        type = "application/x-bittorrent",
                                    ),
                                    guid = "964315057857801",
                                    magneturi = URI("magnet:?xt=urn:btih:f6ae530cb7a93ec8bafc647a6b6b94564c06b0d9&dn=Momochi-san+Chi+no+Ayakashi+Ouji+-+fawea+%5BWEBRip+1080p%5D&xl=15057857801&tr=http%3A%2F%2Ftr.asfd.fun%3A2710%2Fannounce&tr=http%3A%2F%afds.local%2Fannounce")
                                )
                            )
                        )
                    )
                )
        }
    }

    data class TestResponse(
        val title: String
    )

    companion object {
        const val RSS_NON_STANDARD = """<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0">
    <channel>
        <title>Самое свежее с сайта</title>
        <link>https://someurl.it</link>
            <item>
                <title>Принц демонов дома Момочи / серии: 1-10 [WEBRip 1080p] / Momochi-san Chi no Ayakashi Ouji</title>
                <categoy>ТВ (12 эп.), 24 мин.</categoy>
                <description>В день своего шестнадцатилетия сирота Химари Момочи получает прекрасное поместье своих покойных родителей. Для девушки, которая выросла в приюте, это стало замечательной новостью, ведь у неё наконец-то появился собственный уютный уголок, где можно чувствовать себя как дома.
Но если бы всё было так просто! На самом деле, это поместье существует на границе между человеческим и духовным мирами, а сама девушка, будучи его хозяйкой, призвана выступать в качестве хранителя этой границы. Но и это ещё не всё! Как оказалось, в её будущем жилище незаконно поселились трое парней-красавчиков, и теперь они заявляют, что это их дом, а ей лучше бы убираться оттуда подобру-поздорову!&lt;br&gt;&lt;br&gt;</description>
                <link>https://tv3.someurl.it/release/momochi-san-chi-no-ayakashi-ouji</link>
                <magneturi>magnet:?xt=urn:btih:f6ae530cb7a93ec8bafc647a6b6b94564c06b0d9&amp;dn=Momochi-san+Chi+no+Ayakashi+Ouji+-+fawea+%5BWEBRip+1080p%5D&amp;xl=15057857801&amp;tr=http%3A%2F%2Ftr.asfd.fun%3A2710%2Fannounce&amp;tr=http%3A%2F%afds.local%2Fannounce</magneturi>
                <pubDate>2024-03-09 18:52:21</pubDate>
                <contentLength>15057857801</contentLength>
                <guid>964315057857801</guid>
                <enclosure url="https://redirect.asdf.it/upload/torrents/27892.torrent" length="15057857801"
                           type="application/x-bittorrent"/>
            </item>
    </channel>
</rss>
        """
    }
}
