package ru.antonmarin.autoget.infra.transmission.requests

import ru.antonmarin.autoget.infra.transmission.TransmissionClient
import kotlin.reflect.KClass

data class TorrentRenamePathRequest(val ids: Long, val name: String, val path: String = "") :
    TransmissionClient.TransmissionRequest<TorrentRenamePathResponse> {
    override fun provideMethodName(): String = "torrent-rename-path"

    override fun responseClass(): KClass<TorrentRenamePathResponse> = TorrentRenamePathResponse::class
}

data class TorrentRenamePathResponse(val id: Long, val path: String, val name: String)

// rename only "metadataPercentComplete": 1,
