package ru.antonmarin.autoget.infra.plex

import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.actions.TitlesProvider
import ru.antonmarin.autoget.domain.Either
import ru.antonmarin.autoget.infra.xml.JacksonReader
import java.net.URL
import java.net.http.HttpClient

class PlexDiscoveryClient(
    httpClient: HttpClient,
    private val token: String,
) : TitlesProvider {
    private val xmlReader = JacksonReader(httpClient)

    fun getWatchListTitles(): List<String> {
        val watchListMap = xmlReader.read(URL("$WATCH_LIST_URL?X-Plex-Token=$token"), Map::class)
        val directoryKey = "Directory"
        if (!watchListMap.containsKey(directoryKey)) {
            logger.warn("Received unexpected watchlist: $watchListMap")
        }

        @Suppress("UNCHECKED_CAST")
        val directories = (watchListMap[directoryKey] as List<Map<String, String>>).mapNotNull {
            when (val result = mapToDirectory(it)) {
                is Either.Right -> result.value
                is Either.Left -> {
                    logger.warn("Failed mapping Directory: ${result.value}")
                    null
                }
            }
        }

        return directories.map { it.title }
    }

    private fun mapToDirectory(directoryMap: Map<String, String>): Either<ErrorMappingDirectory, Directory> {
        return Directory(
            title = directoryMap["title"] ?: return Either.Left(ErrorMappingDirectory.NO_TITLE),
        ).let { Either.Right(it) }
    }

    enum class ErrorMappingDirectory {
        NO_TITLE,
    }

    override fun getTitles(): List<String> = getWatchListTitles()

    companion object {
        private val logger = LoggerFactory.getLogger(PlexDiscoveryClient::class.java)
        private const val WATCH_LIST_URL = "https://discover.provider.plex.tv/library/sections/watchlist/all"
    }

    data class Directory(
        val title: String,
    )
}
