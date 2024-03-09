package ru.antonmarin.autoget.actions

import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.Action
import ru.antonmarin.autoget.Entry
import ru.antonmarin.autoget.actions.contracts.TorrentClient
import kotlin.collections.List

//todo implement follow redirects
class AddTorrent(
    private val client: TorrentClient,
    private val downloadDir: String? = null,
) : Action {
    override fun execute(entries: List<Entry>): List<Entry> {
        entries.forEach { entry ->
            val torrentUri = entry.torrentUri
            if (torrentUri != null) {
                val torrentId = client.add(torrentUri, downloadDir)
                logger.info("Torrent [$torrentId] ${entry.title} added to transmission")
                entry.torrentId = torrentId
            } else {
                logger.info("Nothing added to Transmission as no torrent uri: $entry")
            }
        }

        return entries
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AddTorrent::class.java)
    }
}
