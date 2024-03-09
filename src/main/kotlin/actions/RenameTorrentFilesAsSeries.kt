package ru.antonmarin.autoget.actions

import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.Action
import ru.antonmarin.autoget.Entry
import ru.antonmarin.autoget.Template
import ru.antonmarin.autoget.actions.contracts.TorrentClient
import ru.antonmarin.autoget.findFirstGroup
import kotlin.collections.List

class RenameTorrentFilesAsSeries(
    private val client: TorrentClient,
    private val episodeNumberRegex: Regex,
    private val titleTemplate: Template,
    private val whenNoMetadata: RenameTorrent.WhenNoMeta = RenameTorrent.Skip(),
) : Action {
    override fun execute(entries: List<Entry>): List<Entry> {
        entries.forEach { entry ->
            val id = entry.torrentId ?: run {
                logger.debug("Skipped entry without torrentId {}", entry)
                return@forEach
            }
            val currentTorrent = whenNoMetadata.getTorrent(client, id) ?: return@forEach
            currentTorrent.files.forEach forEachFiles@{ file ->
                val episodeNumber = episodeNumberRegex.findFirstGroup(file.name) ?: run {
                    logger.debug("file ${file.name} skipped as episode not found")
                    return@forEachFiles
                }
                val title = titleTemplate.replace(
                    titleTemplate.prepareTokens(entry) + mapOf("episodeNumber" to episodeNumber)
                )
                if (client.rename(id, file.name, title)) {
                    logger.info("${file.name} renamed to $title")
                } else {
                    logger.error("Failed renaming ${file.name} to $title")
                }

            }
        }

        return entries
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RenameTorrentFilesAsSeries::class.java)
    }
}
//ids:103
//name:"s01e01]_[AniLibria_TV]_[WEBRip_1080p_HEVC].mkv"
//path:"Ведьма и чудовище [1080p HEVC AniLibria.TV]/Majo_to_Yajuu_[01]_[AniLibria_TV]_[WEBRip_1080p_HEVC].mkv"
//method:"torrent-rename-path"
//tag:96
