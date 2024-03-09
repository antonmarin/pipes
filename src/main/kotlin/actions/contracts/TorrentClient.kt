package ru.antonmarin.autoget.actions.contracts

import java.net.URI

interface TorrentClient {
    fun add(uri: URI, downloadDir: String?): TorrentId
    fun rename(id: TorrentId, from: String, to: String): Boolean
    fun findOne(id: TorrentId): Torrent?
}

typealias TorrentId = Long

class Torrent(
    val id: TorrentId,
    val title: String,
    val metadataReceived: Boolean,
    val files: List<TorrentFile>,
)

class TorrentFile(
    val name: String,
)
