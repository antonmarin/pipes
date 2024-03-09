package ru.antonmarin.autoget.infra.transmission.requests

import com.fasterxml.jackson.annotation.JsonProperty
import ru.antonmarin.autoget.infra.transmission.TransmissionClient
import java.net.URI
import kotlin.reflect.KClass

/**
 * @param filename URI torrent filename or magnet
 *
 * todo implement add using file base64-encoded to metainfo
 */
data class TorrentAddRequest(
    val filename: URI,
    @JsonProperty("download-dir")
    val downloadDir: String? = null
) : TransmissionClient.TransmissionRequest<TorrentAddResponse> {
    override fun provideMethodName(): String = "torrent-add"
    override fun responseClass(): KClass<TorrentAddResponse> = TorrentAddResponse::class
}

data class TorrentAddResponse(
    @JsonProperty("torrent-added")
    val torrentAdded: AddedTorrent?,
    @JsonProperty("torrent-duplicate")
    val torrentDuplicate: AddedTorrent?,
) {
    data class AddedTorrent(
        val hashString: String,
        val id: Long,
        val name: String,
    )
}
