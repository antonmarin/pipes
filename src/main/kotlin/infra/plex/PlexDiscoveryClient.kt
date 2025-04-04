package ru.antonmarin.autoget.infra.plex

import com.fasterxml.jackson.annotation.JsonMerge
import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.actions.TitlesProvider
import ru.antonmarin.autoget.infra.xml.JacksonReader
import java.net.URL
import java.net.http.HttpClient

class PlexDiscoveryClient(
    httpClient: HttpClient,
    private val token: String,
) : TitlesProvider {
    private val xmlReader = JacksonReader(httpClient)

    fun getWatchListTitles(): List<String> {
        val watchListMap = xmlReader.read(URL("$WATCH_LIST_URL?X-Plex-Token=$token"), WatchList::class)
        watchListMap.directories.forEach {
            if(it.banner == null) logger.warn("No banner in directory: $it")
        }

        return watchListMap.directories.flatMap { listOfNotNull(it.title, it.originalTitle) }.also {
            logger.debug("WatchList: {}", it)
        }
    }

    override fun getTitles(): List<String> = getWatchListTitles()

    companion object {
        private val logger = LoggerFactory.getLogger(PlexDiscoveryClient::class.java)
        private const val WATCH_LIST_URL = "https://discover.provider.plex.tv/library/sections/watchlist/all"
    }

    data class Directory(
        val title: String,
        val originalTitle: String?,
        val slug: String,
        val art: URL,
        val banner: URL?,
        val thumb: URL,
        val publicPagesURL: URL,
        val type: String,
    )

    data class WatchList(
        val totalSize: Int,
        val offset: Int,
        val size: Int,
        @JsonProperty("Directory")
        @JsonMerge
        val directories: List<Directory>,
    )
}
