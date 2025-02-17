package ru.antonmarin.autoget

import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.actions.AddAkasList
import ru.antonmarin.autoget.actions.AddTorrent
import ru.antonmarin.autoget.actions.InTitlesList
import ru.antonmarin.autoget.actions.Modify
import ru.antonmarin.autoget.actions.RegExp
import ru.antonmarin.autoget.actions.RenameTorrent
import ru.antonmarin.autoget.actions.RenameTorrentFilesAsSeries
import ru.antonmarin.autoget.actions.RssItems
import ru.antonmarin.autoget.actions.contracts.XmlReader
import ru.antonmarin.autoget.infra.DIContainer
import ru.antonmarin.autoget.infra.db.TvmazeClient
import ru.antonmarin.autoget.infra.plex.PlexDiscoveryClient
import ru.antonmarin.autoget.infra.transmission.TransmissionClient
import ru.antonmarin.autoget.infra.xml.JacksonReader
import ru.antonmarin.autoget.server.Server
import ru.antonmarin.autoget.server.ServerItem
import ru.antonmarin.autoget.server.configuration.ActionConfig
import ru.antonmarin.autoget.server.configuration.ConfigActionFactory
import ru.antonmarin.autoget.server.configuration.ConfigControllerFactory
import ru.antonmarin.autoget.server.configuration.WhenConfig
import java.net.URL
import java.net.http.HttpClient
import java.time.Clock
import java.time.Duration

fun main(args: Array<String>) {
    val logger = LoggerFactory.getLogger(::main.name)
    val clock = Clock.systemDefaultZone()
    logger.debug("Starting with args {} at {}", args, clock)

    val httpClient = HttpClient.newBuilder().build()
    val transmissionClient = TransmissionClient(URL(System.getenv("TRANSMISSION_BASEPATH") + "/transmission/rpc"), httpClient)
    val tvmazeClient = TvmazeClient(httpClient)
    @Suppress("UNUSED_VARIABLE")
    val dic = DIContainer().apply {
        register(XmlReader::class, JacksonReader(httpClient))
    }

    val controllerFactory = ConfigControllerFactory(clock)
    val actionFactory = ConfigActionFactory(JacksonReader(httpClient))
//    val actionFactory = ConfigActionFactory(dic)


    val controllerConfig = WhenConfig(
        `when` = if ("--once" in args) WhenConfig.WhenEnum.ONCE else WhenConfig.WhenEnum.EVERY,
        values = mapOf(
            WhenConfig.ValuesKeys.DELAY_FROM_START.key to "PT1S",
            WhenConfig.ValuesKeys.DELAY_BETWEEN_SUCCESSIVE.key to "PT1H",
        )
    )

    val darklibriaOngoing = ServerItem(
        controller = controllerFactory.create(controllerConfig),
        pipeline = Pipeline(
            listOf(
                actionFactory.create(ActionConfig(mapOf(RssItems::url to System.getenv("TORRENTS_FEED")))),
                RegExp(
                    RegExp.Action.ACCEPT, Entry::title, listOf(
                        Regex(".*HEVC"),
                    )
                ),
                InTitlesList(
                    PlexDiscoveryClient(httpClient, token = System.getenv("PLEX_TOKEN")) // refactor token to config
                        .let { titlesProvider -> AddAkasList(tvmazeClient, titlesProvider) }
                ),
                Modify(Entry::videoResolution, Entry::title, Regex("\\|\\s\\w+\\s(\\d+[pi])")),
                Modify(Entry::title, Entry::title, Regex("([^/]+)\\s/")),
                AddTorrent(
                    client = transmissionClient,
                    downloadDir = "/video/series",
                ),
                Modify(Entry::torrentTitle, "{title} [{videoResolution} HEVC AniLibria.TV]"),
                RenameTorrent(
                    client = transmissionClient,
                    whenNoMetadata = RenameTorrent.Retry(Duration.ofSeconds(5)),
                ),
                RenameTorrentFilesAsSeries(
                    client = transmissionClient,
                    episodeNumberRegex = Regex(".*/[^\\[]+\\[(\\d+)"),
                    titleTemplate = Template("s01e{episodeNumber}.mkv"),
                ),
            )
        )
    )

    Server(
        listOf(
            darklibriaOngoing,
        )
    ).start()
}
