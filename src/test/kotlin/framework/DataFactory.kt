package ru.antonmarin.autoget.framework

import ru.antonmarin.autoget.Entry
import ru.antonmarin.autoget.actions.Enclosure
import ru.antonmarin.autoget.actions.RssItem
import ru.antonmarin.autoget.actions.contracts.Torrent
import ru.antonmarin.autoget.actions.contracts.TorrentFile
import java.net.URI
import java.net.URL

object DataFactory {
    const val MAGNET_URI =
        "magnet:?xt=urn:btih:f6ae530cb7a93ec8bafc647a6b6b94564c06b0d9&dn=Momochi-san+Chi+no+Ayakashi+Ouji+%5BWEBRip+1080p%5D&xl=15057857801&tr=http%3A%2F%2Ftr.asdf.fun%3A2710%2Fannounce&tr=http%3A%2F%2Fretracker.local%2Fannounce"

    fun entry(
        title: String? = null,
        description: String? = null,
        torrentUrl: URI? = null,
        videoResolution: String? = null,
        torrentId: Long? = null,
        torrentTitle: String? = null,
    ) = Entry(
        title = title,
        description = description,
        torrentUri = torrentUrl,
        videoResolution = videoResolution,
        torrentId = torrentId,
        torrentTitle = torrentTitle,
    )

    fun rssItem(
        title: String = "Some title",
        link: URL = URL("http://location"),
        @Suppress("UNUSED_PARAMETER") description: String = "",
        author: String? = null,
        category: String? = null,
        comments: URI? = null,
        enclosure: Enclosure? = null,
        guid: String? = null,
        magneturi: URI? = null,
    ) = RssItem(
        title, link, /*description,*/ author, category, comments, enclosure, guid, magneturi,
    )

    fun torrent(
        id: Long = 3,
        title: String = "Some name",
        metadataReceived: Boolean = false,
        files: List<TorrentFile> = listOf(TorrentFile("some [1080p]/some[03]thing.mkv")),
    ) = Torrent(id, title, metadataReceived, files)
}
