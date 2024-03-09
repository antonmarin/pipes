package ru.antonmarin.autoget.infra.transmission.requests

import com.fasterxml.jackson.annotation.JsonInclude
import ru.antonmarin.autoget.infra.transmission.TransmissionClient
import kotlin.reflect.KClass

/**
 * @link https://github.com/transmission/transmission/blob/main/docs/rpc-spec.md#33-torrent-accessor-torrent-get
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TorrentGetRequest(
    val ids: List<Long>? = null,
    val fields: List<String> = listOf("id", "name", "errorString", "files", "status","metadataPercentComplete"),
) : TransmissionClient.TransmissionRequest<TorrentGetResponse> {
    override fun provideMethodName(): String = "torrent-get"
    override fun responseClass(): KClass<TorrentGetResponse> = TorrentGetResponse::class
}

data class TorrentGetResponse(
    val torrents: List<TorrentResponse>,
)
/**
 * all values can be null when field was not requested
 */
data class TorrentResponse(
    val id: Long?,
    val name: String?,
    val errorString: String?,
    val files: List<TorrentFile>?,
    val status: TorrentStatus?,
    val metadataPercentComplete: Boolean?,
)
data class TorrentFile(
    val name: String,
)
//populates from ordinal values
@Suppress("unused")
enum class TorrentStatus {
    STOPPED,
    QUEUED_TO_VERIFY_LOCAL_DATA,
    VERIFYING_LOCAL_DATA,
    QUEUED_TO_DOWNLOAD,
    DOWNLOADING,
    QUEUED_TO_SEED,
    SEEDING,
}
