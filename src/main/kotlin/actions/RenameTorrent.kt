package ru.antonmarin.autoget.actions

import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.Action
import ru.antonmarin.autoget.Entry
import ru.antonmarin.autoget.actions.contracts.Torrent
import ru.antonmarin.autoget.actions.contracts.TorrentClient
import java.time.Duration
import kotlin.collections.List

class RenameTorrent(
    private val client: TorrentClient,
    private val whenNoMetadata: WhenNoMeta = Skip(),
) : Action {
    override fun execute(entries: List<Entry>): List<Entry> {
        entries.forEach { entry ->
            val id = entry.torrentId ?: run {
                logger.debug("Skipped entry without torrentId {}", entry)
                return@forEach
            }
            val title = entry.torrentTitle ?: run {
                logger.debug("Skipped entry without torrentTitle {}", entry)
                return@forEach
            }

            val currentTorrent = whenNoMetadata.getTorrent(client, id) ?: return@forEach
            if (client.rename(id = id,from = currentTorrent.title,to = title)) {
                logger.info("$id renamed to $title")
            } else {
                logger.error("$id failed to rename")
            }
        }
        return entries
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RenameTorrent::class.java)
    }

    //todo mb replace with client decorator?
    interface WhenNoMeta {
        fun getTorrent(client: TorrentClient, id: Long): Torrent?
    }

    class Retry(private val delay: Duration) : WhenNoMeta {
        override fun getTorrent(client: TorrentClient, id: Long): Torrent? {
            var torrent: Torrent?
            var conditionMet = false
            do {
                torrent = client.findOne(id) ?: run {
                    logger.warn("Torrent $id not found, skipping")
                    return null
                }
                if (!torrent.metadataReceived) {
                    Thread.sleep(delay.toMillis())
                } else {
                    conditionMet = true
                }
            } while (!conditionMet)

            return torrent
        }

        companion object {
            private val logger = LoggerFactory.getLogger(Retry::class.java)
        }
    }

    class Skip : WhenNoMeta {
        override fun getTorrent(client: TorrentClient, id: Long): Torrent? {
            val torrent: Torrent = client.findOne(id) ?: run {
                logger.warn("Torrent $id not found, skipping")
                return null
            }

            if (!torrent.metadataReceived) {
                logger.debug("Torrent ${torrent.id} meta not found, skipping")
                return null
            }

            return torrent
        }

        companion object {
            private val logger = LoggerFactory.getLogger(Skip::class.java)
        }
    }
}
