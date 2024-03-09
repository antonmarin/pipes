package ru.antonmarin.autoget.infra.plex

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File
import java.net.http.HttpClient

class PlexDiscoveryClientTest {
    private val httpClient = mockk<HttpClient>()
    private val client = PlexDiscoveryClient(httpClient, "SomeToken")

    @Nested
    inner class GetWatchListTitlesTest {
        @Test
        fun `should return titles list when success response`() {
            val responseBody = File("src/test/resources/plexWatch.xml").readText()
            every { httpClient.send<String>(any(), any()) } returns mockk {
                every { body() } returns responseBody
            }

            val titles = client.getWatchListTitles()

            Assertions.assertThat(titles).isEqualTo(
                listOf(
                    "Ninja Kamui",
                    "The Witch and the Beast",
                    "Frieren: Beyond Journey's End",
                    "Solo Leveling",
                    "Shangri-La Frontier",
                    "Undead Unluck",
                    "Mr. Villain's Day Off",
                    "Dune",
                    "The Apothecary Diaries",
                    "MASHLE: MAGIC AND MUSCLES",
                    "Monarch: Legacy of Monsters",
                )
            )
        }

        @Test
        fun `should include all Directories when there are another objects between entries`() {
            val responseBody =
                """
                <?xml version="1.0"?>
                <MediaContainer librarySectionID="watchlist" librarySectionTitle="Watchlist" offset="0" totalSize="6" identifier="tv.plex.provider.discover" size="6">
                <Directory art="https://image.tmdb.org/t/p/original/7x7NxuDbtUeZPqK2k4WfQCkeHOc.jpg" banner="https://artworks.thetvdb.com/banners/v4/series/433088/banners/660f45c5605e5.jpg" guid="plex://show/642664835f002b2d715c6be2" key="/library/metadata/642664835f002b2d715c6be2/children" rating="7.7" ratingKey="642664835f002b2d715c6be2" studio="CloverWorks" type="show" thumb="https://image.tmdb.org/t/p/original/3kTFL3PAeTyS8gGZAh0iYG6NNjt.jpg" addedAt="1711843200" duration="1440000" publicPagesURL="https://watch.plex.tv/show/wind-breaker-2" slug="wind-breaker-2" userState="0" title="WIND BREAKER" leafCount="12" childCount="1" isContinuingSeries="1" contentRating="TV-14" originallyAvailableAt="2024-03-31" year="2024" ratingImage="imdb://image.rating" imdbRatingCount="526" source="provider://tv.plex.provider.metadata"><Image alt="WIND BREAKER" type="background" url="https://image.tmdb.org/t/p/original/7x7NxuDbtUeZPqK2k4WfQCkeHOc.jpg"/><Image alt="WIND BREAKER" type="banner" url="https://artworks.thetvdb.com/banners/v4/series/433088/banners/660f45c5605e5.jpg"/><Image alt="WIND BREAKER" type="clearLogo" url="https://metadata-static.plex.tv/9/683a142553/994ca5d58317306a262a4a2228a28d2b.png"/><Image alt="WIND BREAKER" type="clearLogoWide" url="https://metadata-static.plex.tv/9/683a142553/994ca5d58317306a262a4a2228a28d2b.png"/><Image alt="WIND BREAKER" type="coverArt" url="https://image.tmdb.org/t/p/original/qAOaAKrUQW9qRJxtb1YGsMlnBWm.jpg"/><Image alt="WIND BREAKER" type="coverPoster" url="https://image.tmdb.org/t/p/original/3kTFL3PAeTyS8gGZAh0iYG6NNjt.jpg"/></Directory>
                <Video art="https://metadata-static.plex.tv/6/gracenote/658c00b5a3586f0e49534e0a0827f59a.jpg" banner="http://assets.fanart.tv/fanart/movies/567748/moviebanner/the-guilty-615899f1de79e.jpg" guid="plex://movie/5d7770a981ba41001faf5335" key="/library/metadata/5d7770a981ba41001faf5335" primaryExtraKey="/library/metadata/5d7770a981ba41001faf5335/extras/6209819844f483344cf78534" rating="7.4" ratingKey="5d7770a981ba41001faf5335" studio="Nine Stories Productions" tagline="Listen carefully." type="movie" thumb="https://metadata-static.plex.tv/8/gracenote/8b3e8b7ac38600989305d20e685a7b7b.jpg" addedAt="1633046400" duration="5460000" publicPagesURL="https://watch.plex.tv/movie/the-guilty-2021" slug="the-guilty-2021" userState="0" title="The Guilty" contentRating="R" originallyAvailableAt="2021-10-01" year="2021" audienceRating="4.9" audienceRatingImage="rottentomatoes://image.rating.spilled" ratingImage="rottentomatoes://image.rating.ripe" imdbRatingCount="148870" source="provider://tv.plex.provider.metadata"><Image alt="The Guilty" type="background" url="https://metadata-static.plex.tv/6/gracenote/658c00b5a3586f0e49534e0a0827f59a.jpg"/><Image alt="The Guilty" type="banner" url="http://assets.fanart.tv/fanart/movies/567748/moviebanner/the-guilty-615899f1de79e.jpg"/><Image alt="The Guilty" type="clearLogo" url="https://metadata-static.plex.tv/d/683a142553/d1c63b03508d151b39fb50edee2ddbd1.png"/><Image alt="The Guilty" type="clearLogoWide" url="https://metadata-static.plex.tv/d/683a142553/d1c63b03508d151b39fb50edee2ddbd1.png"/><Image alt="The Guilty" type="coverArt" url="https://metadata-static.plex.tv/c/gracenote/c24491a22198cbd0b78833f7bf718b81.jpg"/><Image alt="The Guilty" type="coverPoster" url="https://metadata-static.plex.tv/8/gracenote/8b3e8b7ac38600989305d20e685a7b7b.jpg"/><Image alt="The Guilty" type="coverSquare" url="https://metadata-static.plex.tv/2/gracenote/232f67eba413c184a22e696ea070dcb6.jpg"/><Image alt="The Guilty" type="snapshot" url="https://metadata-static.plex.tv/f/gracenote/ff2cd82b314ec7ffdfd73d32ab9e1d2e.jpg"/></Video>
                <Video art="https://image.tmdb.org/t/p/original/lIUIATeMilUoLzl6NyXtnKx0RRI.jpg" guid="plex://movie/5e1624dd53953d003edee5c0" key="/library/metadata/5e1624dd53953d003edee5c0" rating="6.4" ratingKey="5e1624dd53953d003edee5c0" studio="Stowarzyszenie Artystyczne KMT" type="movie" thumb="https://image.tmdb.org/t/p/original/u3HZWJLqvV1x3EeHesanJYcWebU.jpg" addedAt="1575072000" duration="6180000" publicPagesURL="https://watch.plex.tv/movie/alzurs-legacy" slug="alzurs-legacy" userState="0" title="Half a Century of Poetry Later" originalTitle="Pół wieku poezji później" originallyAvailableAt="2019-11-30" year="2019" ratingImage="imdb://image.rating" imdbRatingCount="559" source="provider://tv.plex.provider.metadata"><Image alt="Alzur&apos;s Legacy" type="background" url="https://image.tmdb.org/t/p/original/lIUIATeMilUoLzl6NyXtnKx0RRI.jpg"/><Image alt="Alzur&apos;s Legacy" type="coverPoster" url="https://image.tmdb.org/t/p/original/aKTIu3IIXouwCcNfB99w2pSK4P.jpg"/></Video>
                <Directory art="https://image.tmdb.org/t/p/original/t8KjRqfOqNx14cHLwARjR08bjeb.jpg" banner="https://artworks.thetvdb.com/banners/graphical/5dbbc1f5b2b50.jpg" guid="plex://show/5d9c080fec357c001f9a97e6" key="/library/metadata/5d9c080fec357c001f9a97e6/children" primaryExtraKey="/library/metadata/5d9c080fec357c001f9a97e6/extras/620466ae4534baa9d476cfd6" rating="7.8" ratingKey="5d9c080fec357c001f9a97e6" studio="Bad Wolf" tagline="The Truth Lies Beyond the World We Know" type="show" theme="https://tvthemes.plexapp.com/360295.mp3" thumb="https://image.tmdb.org/t/p/original/1ljcoM9hFNiXpcoevZQwwc7oCYT.jpg" addedAt="1572739200" duration="3180000" publicPagesURL="https://watch.plex.tv/show/his-dark-materials" slug="his-dark-materials" userState="0" title="His Dark Materials" leafCount="40" childCount="3" contentRating="TV-14" originallyAvailableAt="2019-11-03" year="2019" ratingImage="imdb://image.rating" imdbRatingCount="84686" source="provider://tv.plex.provider.metadata"><Image alt="His Dark Materials" type="background" url="https://image.tmdb.org/t/p/original/t8KjRqfOqNx14cHLwARjR08bjeb.jpg"/><Image alt="His Dark Materials" type="banner" url="https://artworks.thetvdb.com/banners/graphical/5dbbc1f5b2b50.jpg"/><Image alt="His Dark Materials" type="clearLogo" url="https://metadata-static.plex.tv/3/683a142553/38b3e6a06dbaaa65063ca198a71b245c.png"/><Image alt="His Dark Materials" type="clearLogoWide" url="https://metadata-static.plex.tv/3/683a142553/38b3e6a06dbaaa65063ca198a71b245c.png"/><Image alt="His Dark Materials" type="coverArt" url="https://metadata-static.plex.tv/5/gracenote/561bfe24f94204ce2a11b08bf969a5e3.jpg"/><Image alt="His Dark Materials" type="coverPoster" url="https://image.tmdb.org/t/p/original/1ljcoM9hFNiXpcoevZQwwc7oCYT.jpg"/><Image alt="His Dark Materials" type="coverSquare" url="https://metadata-static.plex.tv/c/gracenote/c32aca056ffc93dc011892ececcbf04d.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/3nmDXGCDcHbtP3Rw4vi9RD3cmmX.jpg" banner="https://artworks.thetvdb.com/banners/v4/series/420280/banners/65babe633cd78.jpg" guid="plex://show/62866cfebf6db0dac5cdaee6" key="/library/metadata/62866cfebf6db0dac5cdaee6/children" primaryExtraKey="/library/metadata/62866cfebf6db0dac5cdaee6/extras/65d3d956f334bfa3a927ebb9" rating="8" ratingKey="62866cfebf6db0dac5cdaee6" studio="E&amp;H production" type="show" thumb="https://image.tmdb.org/t/p/original/3dl7QFrpvtu9My4L6K7KtUMP8p1.jpg" addedAt="1707523200" duration="1380000" publicPagesURL="https://watch.plex.tv/show/ninja-kamui" slug="ninja-kamui" userState="0" title="Ninja Kamui" leafCount="13" childCount="1" isContinuingSeries="1" contentRating="TV-MA" originallyAvailableAt="2024-02-10" year="2024" ratingImage="imdb://image.rating" imdbRatingCount="4700" source="provider://tv.plex.provider.metadata"><Image alt="Ninja Kamui" type="background" url="https://image.tmdb.org/t/p/original/3nmDXGCDcHbtP3Rw4vi9RD3cmmX.jpg"/><Image alt="Ninja Kamui" type="banner" url="https://artworks.thetvdb.com/banners/v4/series/420280/banners/65babe633cd78.jpg"/><Image alt="Ninja Kamui" type="clearLogo" url="https://metadata-static.plex.tv/b/009125463f/b476240b40158fd4b3c97768964a76c5.png"/><Image alt="Ninja Kamui" type="clearLogoWide" url="https://metadata-static.plex.tv/b/009125463f/b476240b40158fd4b3c97768964a76c5.png"/><Image alt="Ninja Kamui" type="coverArt" url="https://metadata-static.plex.tv/6/gracenote/65d94214c474f120de9589fe1a1b1fc0.jpg"/><Image alt="Ninja Kamui" type="coverPoster" url="https://image.tmdb.org/t/p/original/3dl7QFrpvtu9My4L6K7KtUMP8p1.jpg"/><Image alt="Ninja Kamui" type="coverSquare" url="https://metadata-static.plex.tv/c/gracenote/c22723458f882b68e2fba2113f709bfc.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/lghMCJJ7u2fIHMVtsrsMCV9OTsI.jpg" banner="https://artworks.thetvdb.com/banners/v4/series/422598/banners/65870d3444ee9.jpg" guid="plex://show/629239d5227b9116a93569f0" key="/library/metadata/629239d5227b9116a93569f0/children" primaryExtraKey="/library/metadata/629239d5227b9116a93569f0/extras/64fbd083220d559400e57988" rating="7" ratingKey="629239d5227b9116a93569f0" studio="Legendary Television" tagline="Some secrets cannot be contained." type="show" theme="https://tvthemes.plexapp.com/422598.mp3" thumb="https://image.tmdb.org/t/p/original/uwrQHMnXD2DA1rvaMZk4pavZ3CY.jpg" addedAt="1700179200" duration="2700000" publicPagesURL="https://watch.plex.tv/show/untitled-apple-godzilla-and-the-titans-project" slug="untitled-apple-godzilla-and-the-titans-project" userState="0" title="Monarch: Legacy of Monsters" leafCount="10" childCount="1" isContinuingSeries="1" contentRating="TV-14" originallyAvailableAt="2023-11-17" year="2023" ratingImage="imdb://image.rating" imdbRatingCount="40455" source="provider://tv.plex.provider.metadata"><Image alt="Monarch: Legacy of Monsters" type="background" url="https://image.tmdb.org/t/p/original/lghMCJJ7u2fIHMVtsrsMCV9OTsI.jpg"/><Image alt="Monarch: Legacy of Monsters" type="banner" url="https://artworks.thetvdb.com/banners/v4/series/422598/banners/65870d3444ee9.jpg"/><Image alt="Monarch: Legacy of Monsters" type="clearLogo" url="https://metadata-static.plex.tv/8/683a142553/870248ff540f2173b811c2ad6c8adf30.png"/><Image alt="Monarch: Legacy of Monsters" type="clearLogoWide" url="https://metadata-static.plex.tv/8/683a142553/870248ff540f2173b811c2ad6c8adf30.png"/><Image alt="Monarch: Legacy of Monsters" type="coverArt" url="https://metadata-static.plex.tv/f/gracenote/f9bcfe89e8d3b057367fc1b5a9b7e60e.jpg"/><Image alt="Monarch: Legacy of Monsters" type="coverPoster" url="https://image.tmdb.org/t/p/original/uwrQHMnXD2DA1rvaMZk4pavZ3CY.jpg"/><Image alt="Monarch: Legacy of Monsters" type="coverSquare" url="https://metadata-static.plex.tv/1/gracenote/13d6951d694a79fc83131ee177e5ed0f.jpg"/></Directory>
                </MediaContainer>
            """.trimIndent()
            every { httpClient.send<String>(any(), any()) } returns mockk {
                every { body() } returns responseBody
            }

            val titles = client.getWatchListTitles()

            Assertions.assertThat(titles).isEqualTo(
                listOf(
                    "WIND BREAKER",
                    "His Dark Materials",
                    "Ninja Kamui",
                    "Monarch: Legacy of Monsters",
                )
            )
        }
    }
}
