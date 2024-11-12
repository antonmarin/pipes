package ru.antonmarin.autoget.infra.plex

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.http.HttpClient

class PlexDiscoveryClientTest {
    private val httpClient = mockk<HttpClient>()
    private val client = PlexDiscoveryClient(httpClient, "SomeToken")

    @Nested
    inner class GetWatchListTitlesTest {
        @Test
        fun `should return titles list when success response`() {
            val responseBody =
                """
                <?xml version="1.0"?>
                <MediaContainer librarySectionID="watchlist" librarySectionTitle="Watchlist" offset="0" totalSize="11" identifier="tv.plex.provider.discover" size="11">
                <Directory art="https://image.tmdb.org/t/p/original/3nmDXGCDcHbtP3Rw4vi9RD3cmmX.jpg" banner="https://artworks.thetvdb.com/banners/v4/series/420280/banners/65babe633cd78.jpg" guid="plex://show/62866cfebf6db0dac5cdaee6" key="/library/metadata/62866cfebf6db0dac5cdaee6/children" primaryExtraKey="/library/metadata/62866cfebf6db0dac5cdaee6/extras/65d3d956f334bfa3a927ebb9" rating="8.3" ratingKey="62866cfebf6db0dac5cdaee6" studio="E&amp;H production" type="show" thumb="https://image.tmdb.org/t/p/original/3dl7QFrpvtu9My4L6K7KtUMP8p1.jpg" addedAt="1707523200" duration="1380000" publicPagesURL="https://watch.plex.tv/show/ninja-kamui" slug="ninja-kamui" userState="0" title="Ninja Kamui" leafCount="12" childCount="1" isContinuingSeries="1" contentRating="TV-MA" originallyAvailableAt="2024-02-10" year="2024" ratingImage="imdb://image.rating" imdbRatingCount="3629" source="provider://tv.plex.provider.metadata"><Image alt="Ninja Kamui" type="background" url="https://image.tmdb.org/t/p/original/3nmDXGCDcHbtP3Rw4vi9RD3cmmX.jpg"/><Image alt="Ninja Kamui" type="banner" url="https://artworks.thetvdb.com/banners/v4/series/420280/banners/65babe633cd78.jpg"/><Image alt="Ninja Kamui" type="clearLogo" url="https://metadata-static.plex.tv/b/009125463f/b476240b40158fd4b3c97768964a76c5.png"/><Image alt="Ninja Kamui" type="clearLogoWide" url="https://metadata-static.plex.tv/b/009125463f/b476240b40158fd4b3c97768964a76c5.png"/><Image alt="Ninja Kamui" type="coverArt" url="https://metadata-static.plex.tv/6/gracenote/65d94214c474f120de9589fe1a1b1fc0.jpg"/><Image alt="Ninja Kamui" type="coverPoster" url="https://image.tmdb.org/t/p/original/3dl7QFrpvtu9My4L6K7KtUMP8p1.jpg"/><Image alt="Ninja Kamui" type="coverSquare" url="https://metadata-static.plex.tv/c/gracenote/c22723458f882b68e2fba2113f709bfc.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/3sOWWRpSEGrljd8O50KLmOIdIPn.jpg" banner="https://artworks.thetvdb.com/banners/v4/series/423601/banners/65a0707d0a0aa.jpg" guid="plex://show/6301d4122bd2c2b545c7a7fc" key="/library/metadata/6301d4122bd2c2b545c7a7fc/children" rating="6.5" ratingKey="6301d4122bd2c2b545c7a7fc" studio="TBS" type="show" thumb="https://image.tmdb.org/t/p/original/ooSzpaJkU8MnfuLKn0IThcACv57.jpg" addedAt="1705017600" duration="1440000" publicPagesURL="https://watch.plex.tv/show/witch-and-the-beast" slug="witch-and-the-beast" userState="0" title="The Witch and the Beast" originalTitle="魔女と野獣" leafCount="12" childCount="1" isContinuingSeries="1" contentRating="TV-14" originallyAvailableAt="2024-01-12" year="2024" ratingImage="imdb://image.rating" imdbRatingCount="318" source="provider://tv.plex.provider.metadata"><Image alt="The Witch and the Beast" type="background" url="https://image.tmdb.org/t/p/original/3sOWWRpSEGrljd8O50KLmOIdIPn.jpg"/><Image alt="The Witch and the Beast" type="banner" url="https://artworks.thetvdb.com/banners/v4/series/423601/banners/65a0707d0a0aa.jpg"/><Image alt="The Witch and the Beast" type="coverArt" url="https://metadata-static.plex.tv/b/gracenote/b0517af5c24bee08e2806d5668afc6f8.jpg"/><Image alt="The Witch and the Beast" type="coverPoster" url="https://image.tmdb.org/t/p/original/ooSzpaJkU8MnfuLKn0IThcACv57.jpg"/><Image alt="The Witch and the Beast" type="coverSquare" url="https://metadata-static.plex.tv/6/gracenote/6b16e864273ae8c1abc345e9add5496b.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/96RT2A47UdzWlUfvIERFyBsLhL2.jpg" banner="https://artworks.thetvdb.com/banners/v4/series/424536/banners/651dd688bd4d5.jpg" guid="plex://show/631ccdfccbff4464978f9d53" key="/library/metadata/631ccdfccbff4464978f9d53/children" rating="9" ratingKey="631ccdfccbff4464978f9d53" studio="Madhouse" type="show" theme="https://tvthemes.plexapp.com/424536.mp3" thumb="https://image.tmdb.org/t/p/original/dqZENchTd7lp5zht7BdlqM7RBhD.jpg" addedAt="1695945600" duration="1500000" publicPagesURL="https://watch.plex.tv/show/frieren-beyond-journeys-end" slug="frieren-beyond-journeys-end" userState="0" title="Frieren: Beyond Journey&apos;s End" originalTitle="葬送のフリーレン" leafCount="41" childCount="1" contentRating="TV-14" originallyAvailableAt="2023-09-29" year="2023" ratingImage="imdb://image.rating" imdbRatingCount="10773" source="provider://tv.plex.provider.metadata"><Image alt="Frieren: Beyond Journey&apos;s End" type="background" url="https://image.tmdb.org/t/p/original/96RT2A47UdzWlUfvIERFyBsLhL2.jpg"/><Image alt="Frieren: Beyond Journey&apos;s End" type="banner" url="https://artworks.thetvdb.com/banners/v4/series/424536/banners/651dd688bd4d5.jpg"/><Image alt="Frieren: Beyond Journey&apos;s End" type="clearLogo" url="https://metadata-static.plex.tv/b/683a142553/bfd789fb3377bd8fdcd31d0a8a323a29.png"/><Image alt="Frieren: Beyond Journey&apos;s End" type="clearLogoWide" url="https://metadata-static.plex.tv/b/683a142553/bfd789fb3377bd8fdcd31d0a8a323a29.png"/><Image alt="Frieren: Beyond Journey&apos;s End" type="coverArt" url="https://metadata-static.plex.tv/e/gracenote/e7a51d9702822d5666eb6cac0bcfdc50.jpg"/><Image alt="Frieren: Beyond Journey&apos;s End" type="coverPoster" url="https://image.tmdb.org/t/p/original/dqZENchTd7lp5zht7BdlqM7RBhD.jpg"/><Image alt="Frieren: Beyond Journey&apos;s End" type="coverSquare" url="https://metadata-static.plex.tv/f/gracenote/fe11644c1d4348a4b8735916d46a4bb2.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/odVlTMqPPiMksmxpN9cCbPCjUPP.jpg" banner="https://artworks.thetvdb.com/banners/v4/series/389597/banners/613e74b11a9f0.jpg" guid="plex://show/60c8f1eb65616d002c4c7951" key="/library/metadata/60c8f1eb65616d002c4c7951/children" rating="8.4" ratingKey="60c8f1eb65616d002c4c7951" studio="A-1 Pictures" type="show" theme="https://tvthemes.plexapp.com/389597.mp3" thumb="https://image.tmdb.org/t/p/original/a7i9OdTUo9jZ1XoraCRIQNJ6ACX.jpg" addedAt="1704499200" duration="1440000" publicPagesURL="https://watch.plex.tv/show/solo-leveling-2" slug="solo-leveling-2" userState="0" title="Solo Leveling" originalTitle="俺だけレベルアップな件" leafCount="13" childCount="1" isContinuingSeries="1" contentRating="TV-14" originallyAvailableAt="2024-01-06" year="2024" ratingImage="imdb://image.rating" imdbRatingCount="12531" source="provider://tv.plex.provider.metadata"><Image alt="Solo Leveling" type="background" url="https://image.tmdb.org/t/p/original/odVlTMqPPiMksmxpN9cCbPCjUPP.jpg"/><Image alt="Solo Leveling" type="banner" url="https://artworks.thetvdb.com/banners/v4/series/389597/banners/613e74b11a9f0.jpg"/><Image alt="Solo Leveling" type="clearLogo" url="https://metadata-static.plex.tv/b/683a142553/bf09bb09909ab1506535656118139e12.png"/><Image alt="Solo Leveling" type="clearLogoWide" url="https://metadata-static.plex.tv/b/683a142553/bf09bb09909ab1506535656118139e12.png"/><Image alt="Solo Leveling" type="coverArt" url="https://metadata-static.plex.tv/3/gracenote/3a46a46130236b9bc1213ac96fe67f2f.jpg"/><Image alt="Solo Leveling" type="coverPoster" url="https://image.tmdb.org/t/p/original/a7i9OdTUo9jZ1XoraCRIQNJ6ACX.jpg"/><Image alt="Solo Leveling" type="coverSquare" url="https://metadata-static.plex.tv/5/gracenote/546411226b9b024b18a8a3b6d120b930.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/yErVUZkLVak2ICxFC7mMfl3vcNP.jpg" banner="https://artworks.thetvdb.com/banners/v4/series/421855/banners/65c003ba72524.jpg" guid="plex://show/62bdf2e671a2d41681145662" key="/library/metadata/62bdf2e671a2d41681145662/children" rating="7.6" ratingKey="62bdf2e671a2d41681145662" studio="C2C" tagline="&quot;Listen, you want to know the truth about the world?&quot;" type="show" theme="https://tvthemes.plexapp.com/421855.mp3" thumb="https://image.tmdb.org/t/p/original/aCGdpgNkgz66R1winFkTFsMAhlC.jpg" addedAt="1696118400" duration="1440000" publicPagesURL="https://watch.plex.tv/show/shangri-la-frontier" slug="shangri-la-frontier" userState="0" title="Shangri-La Frontier" originalTitle="シャングリラ・フロンティア" leafCount="35" childCount="1" isContinuingSeries="1" contentRating="TV-PG" originallyAvailableAt="2023-10-01" year="2023" ratingImage="imdb://image.rating" imdbRatingCount="1206" source="provider://tv.plex.provider.metadata"><Image alt="Shangri-La Frontier" type="background" url="https://image.tmdb.org/t/p/original/yErVUZkLVak2ICxFC7mMfl3vcNP.jpg"/><Image alt="Shangri-La Frontier" type="banner" url="https://artworks.thetvdb.com/banners/v4/series/421855/banners/65c003ba72524.jpg"/><Image alt="Shangri-La Frontier" type="clearLogo" url="https://metadata-static.plex.tv/9/683a142553/97b49479cf1f1cdab5445eaf77ffb182.png"/><Image alt="Shangri-La Frontier" type="clearLogoWide" url="https://metadata-static.plex.tv/9/683a142553/97b49479cf1f1cdab5445eaf77ffb182.png"/><Image alt="Shangri-La Frontier" type="coverArt" url="https://metadata-static.plex.tv/3/gracenote/36c7f9ee40dd5bd030eb76cb1daa91ca.jpg"/><Image alt="Shangri-La Frontier" type="coverPoster" url="https://image.tmdb.org/t/p/original/j39TyyUjlOd1lYUUGWh7jIza9jn.jpg"/><Image alt="Shangri-La Frontier" type="coverSquare" url="https://metadata-static.plex.tv/8/gracenote/808a9c6a813ffb3ccf9746ef647647ca.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/8uoNGqXeJCBoG2gLOsKW3qUDuaI.jpg" banner="http://assets.fanart.tv/fanart/tv/423030/tvbanner/undead-unluck-64dedc9e6c484.jpg" guid="plex://show/630c9b650d39af2d0e527916" key="/library/metadata/630c9b650d39af2d0e527916/children" rating="7.4" ratingKey="630c9b650d39af2d0e527916" studio="David Production" type="show" theme="https://tvthemes.plexapp.com/423030.mp3" thumb="https://image.tmdb.org/t/p/original/vcd9WHPHOEoiFEbz2EBN58IT7ab.jpg" addedAt="1696550400" duration="1440000" publicPagesURL="https://watch.plex.tv/show/undead-unluck" slug="undead-unluck" userState="0" title="Undead Unluck" originalTitle="アンデッドアンラック" leafCount="24" childCount="1" isContinuingSeries="1" contentRating="TV-MA" originallyAvailableAt="2023-10-06" year="2023" ratingImage="imdb://image.rating" imdbRatingCount="1329" source="provider://tv.plex.provider.metadata"><Image alt="Undead Unluck" type="background" url="https://image.tmdb.org/t/p/original/8uoNGqXeJCBoG2gLOsKW3qUDuaI.jpg"/><Image alt="Undead Unluck" type="banner" url="http://assets.fanart.tv/fanart/tv/423030/tvbanner/undead-unluck-64dedc9e6c484.jpg"/><Image alt="Undead Unluck" type="coverArt" url="https://metadata-static.plex.tv/5/gracenote/5ea157e39759d04ece3f811c8de6cb42.jpg"/><Image alt="Undead Unluck" type="coverPoster" url="https://image.tmdb.org/t/p/original/pJYOYFlu4Vayz5xXJJuP7ViwHnT.jpg"/><Image alt="Undead Unluck" type="coverSquare" url="https://metadata-static.plex.tv/c/gracenote/cbff89181586e8dfee94866626ae7920.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/dieAKtr4AuTaANZ9B54dt38RKyi.jpg" banner="http://assets.fanart.tv/fanart/tv/431499/tvbanner/mr-villains-day-off-6561c8cdca012.jpg" guid="plex://show/63f9cec5b0908c4032814ef5" key="/library/metadata/63f9cec5b0908c4032814ef5/children" rating="6.4" ratingKey="63f9cec5b0908c4032814ef5" studio="Shin-Ei Animation" type="show" theme="https://tvthemes.plexapp.com/431499.mp3" thumb="https://image.tmdb.org/t/p/original/qilXdGCROmS07J0rnqYYusPa0KY.jpg" addedAt="1704585600" duration="1440000" publicPagesURL="https://watch.plex.tv/show/mr-villains-day-off" slug="mr-villains-day-off" userState="0" title="Mr. Villain&apos;s Day Off" originalTitle="休日のわるものさん" leafCount="12" childCount="1" contentRating="Not Rated" originallyAvailableAt="2024-01-07" year="2024" ratingImage="imdb://image.rating" imdbRatingCount="126" source="provider://tv.plex.provider.metadata"><Image alt="Mr. Villain&apos;s Day Off" type="background" url="https://image.tmdb.org/t/p/original/dieAKtr4AuTaANZ9B54dt38RKyi.jpg"/><Image alt="Mr. Villain&apos;s Day Off" type="banner" url="http://assets.fanart.tv/fanart/tv/431499/tvbanner/mr-villains-day-off-6561c8cdca012.jpg"/><Image alt="Mr. Villain&apos;s Day Off" type="clearLogo" url="https://metadata-static.plex.tv/a/683a142553/a15e250922dddfb22759102a1cd58b1c.png"/><Image alt="Mr. Villain&apos;s Day Off" type="clearLogoWide" url="https://metadata-static.plex.tv/a/683a142553/a15e250922dddfb22759102a1cd58b1c.png"/><Image alt="Mr. Villain&apos;s Day Off" type="coverArt" url="https://image.tmdb.org/t/p/original/pU6r1P6VsekIZgK2FS36OUatnEn.jpg"/><Image alt="Mr. Villain&apos;s Day Off" type="coverPoster" url="https://image.tmdb.org/t/p/original/qilXdGCROmS07J0rnqYYusPa0KY.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/4TpDp2hueKimqgC8hXweZW0T0yW.jpg" banner="https://artworks.thetvdb.com/banners/graphical/79551-g.jpg" guid="plex://show/5d9c08444eefaa001f5d9359" key="/library/metadata/5d9c08444eefaa001f5d9359/children" primaryExtraKey="/library/metadata/5d9c08444eefaa001f5d9359/extras/62069a2c8087ab25908f8c45" rating="6.9" ratingKey="5d9c08444eefaa001f5d9359" studio="Victor Television Productions Inc." subtype="miniSeries" tagline="Discover the greatest treasure in the universe." type="show" theme="https://tvthemes.plexapp.com/79551.mp3" thumb="https://image.tmdb.org/t/p/original/iIf5XeuuBKPtG6H7MfWLirbJGrQ.jpg" addedAt="975801600" duration="5700000" publicPagesURL="https://watch.plex.tv/show/dune" slug="dune" userState="0" title="Dune" leafCount="10" childCount="1" skipChildren="1" contentRating="TV-14" originallyAvailableAt="2000-12-03" year="2000" ratingImage="imdb://image.rating" imdbRatingCount="24619" source="provider://tv.plex.provider.metadata"><Image alt="Dune" type="background" url="https://image.tmdb.org/t/p/original/4TpDp2hueKimqgC8hXweZW0T0yW.jpg"/><Image alt="Dune" type="banner" url="https://artworks.thetvdb.com/banners/graphical/79551-g.jpg"/><Image alt="Dune" type="clearLogo" url="https://metadata-static.plex.tv/5/683a142553/5055284b756fd3ed344686ccfd51eabf.png"/><Image alt="Dune" type="clearLogoWide" url="https://metadata-static.plex.tv/5/683a142553/5055284b756fd3ed344686ccfd51eabf.png"/><Image alt="Dune" type="coverArt" url="https://image.tmdb.org/t/p/original/xO7JoyFhT7pQ9HHAqn5lkEd9CCr.jpg"/><Image alt="Dune" type="coverPoster" url="https://image.tmdb.org/t/p/original/iIf5XeuuBKPtG6H7MfWLirbJGrQ.jpg"/><Image alt="Dune" type="coverSquare" url="https://metadata-static.plex.tv/a/gracenote/a833856a487917484ff7b80d225ac878.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/lJuQBW4w1x6NgD514xDPqZ2Lbpz.jpg" banner="https://artworks.thetvdb.com/banners/v4/series/431162/banners/6545d53424485.jpg" guid="plex://show/63edda20eaab3bc0ed18bff2" key="/library/metadata/63edda20eaab3bc0ed18bff2/children" rating="8.6" ratingKey="63edda20eaab3bc0ed18bff2" studio="OLM" type="show" theme="https://tvthemes.plexapp.com/431162.mp3" thumb="https://image.tmdb.org/t/p/original/e3ojpANrFnmJCyeBNTinYwyBCIN.jpg" addedAt="1697846400" duration="1380000" publicPagesURL="https://watch.plex.tv/show/the-apothecary-diaries" slug="the-apothecary-diaries" userState="0" title="The Apothecary Diaries" originalTitle="薬屋のひとりごと" leafCount="48" childCount="2" isContinuingSeries="1" contentRating="TV-14" originallyAvailableAt="2023-10-21" year="2023" ratingImage="imdb://image.rating" imdbRatingCount="2990" source="provider://tv.plex.provider.metadata"><Image alt="The Apothecary Diaries" type="background" url="https://image.tmdb.org/t/p/original/lJuQBW4w1x6NgD514xDPqZ2Lbpz.jpg"/><Image alt="The Apothecary Diaries" type="banner" url="https://artworks.thetvdb.com/banners/v4/series/431162/banners/6545d53424485.jpg"/><Image alt="The Apothecary Diaries" type="coverArt" url="https://metadata-static.plex.tv/d/gracenote/d2987c8506f03dcab42cb47bb3c4c910.jpg"/><Image alt="The Apothecary Diaries" type="coverPoster" url="https://image.tmdb.org/t/p/original/e3ojpANrFnmJCyeBNTinYwyBCIN.jpg"/><Image alt="The Apothecary Diaries" type="coverSquare" url="https://metadata-static.plex.tv/3/gracenote/3759e0fd7129553a119b2e1e982d4ccc.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/p1swd15DRtCnNj20U904dbXeVsi.jpg" banner="https://artworks.thetvdb.com/banners/v4/series/421737/banners/651de695e80ca.jpg" guid="plex://show/632f3453b625a2252ef53517" key="/library/metadata/632f3453b625a2252ef53517/children" rating="7.6" ratingKey="632f3453b625a2252ef53517" studio="A-1 Pictures" type="show" theme="https://tvthemes.plexapp.com/421737.mp3" thumb="https://image.tmdb.org/t/p/original/j4uI3VfubinCGzatlZcRcjrLyZ1.jpg" addedAt="1680825600" duration="1440000" publicPagesURL="https://watch.plex.tv/show/mashle-magic-and-muscles" slug="mashle-magic-and-muscles" userState="0" title="MASHLE: MAGIC AND MUSCLES" originalTitle="マッシュル-MASHLE-" leafCount="26" childCount="1" isContinuingSeries="1" contentRating="TV-14" originallyAvailableAt="2023-04-07" year="2023" ratingImage="imdb://image.rating" imdbRatingCount="6090" source="provider://tv.plex.provider.metadata"><Image alt="MASHLE: MAGIC AND MUSCLES" type="background" url="https://image.tmdb.org/t/p/original/p1swd15DRtCnNj20U904dbXeVsi.jpg"/><Image alt="MASHLE: MAGIC AND MUSCLES" type="banner" url="https://artworks.thetvdb.com/banners/v4/series/421737/banners/651de695e80ca.jpg"/><Image alt="MASHLE: MAGIC AND MUSCLES" type="clearLogo" url="https://metadata-static.plex.tv/5/683a142553/5223e8ed7b8177b4998f94c8168b5e2e.png"/><Image alt="MASHLE: MAGIC AND MUSCLES" type="clearLogoWide" url="https://metadata-static.plex.tv/5/683a142553/5223e8ed7b8177b4998f94c8168b5e2e.png"/><Image alt="MASHLE: MAGIC AND MUSCLES" type="coverArt" url="https://image.tmdb.org/t/p/original/bGebBe1NuLRgQfDAMzzAPcskanE.jpg"/><Image alt="MASHLE: MAGIC AND MUSCLES" type="coverPoster" url="https://image.tmdb.org/t/p/original/j4uI3VfubinCGzatlZcRcjrLyZ1.jpg"/><Image alt="MASHLE: MAGIC AND MUSCLES" type="coverSquare" url="https://metadata-static.plex.tv/1/gracenote/19e790bd33c0ccf3ec043b3495d3177c.jpg"/></Directory>
                <Directory art="https://image.tmdb.org/t/p/original/lghMCJJ7u2fIHMVtsrsMCV9OTsI.jpg" banner="https://artworks.thetvdb.com/banners/v4/series/422598/banners/65870d3444ee9.jpg" guid="plex://show/629239d5227b9116a93569f0" key="/library/metadata/629239d5227b9116a93569f0/children" primaryExtraKey="/library/metadata/629239d5227b9116a93569f0/extras/64fbd083220d559400e57988" rating="7" ratingKey="629239d5227b9116a93569f0" studio="Legendary Television" tagline="Some secrets cannot be contained." type="show" theme="https://tvthemes.plexapp.com/422598.mp3" thumb="https://image.tmdb.org/t/p/original/uwrQHMnXD2DA1rvaMZk4pavZ3CY.jpg" addedAt="1700179200" duration="2700000" publicPagesURL="https://watch.plex.tv/show/untitled-apple-godzilla-and-the-titans-project" slug="untitled-apple-godzilla-and-the-titans-project" userState="0" title="Monarch: Legacy of Monsters" leafCount="10" childCount="1" isContinuingSeries="1" contentRating="TV-14" originallyAvailableAt="2023-11-17" year="2023" ratingImage="imdb://image.rating" imdbRatingCount="37539" source="provider://tv.plex.provider.metadata"><Image alt="Monarch: Legacy of Monsters" type="background" url="https://image.tmdb.org/t/p/original/lghMCJJ7u2fIHMVtsrsMCV9OTsI.jpg"/><Image alt="Monarch: Legacy of Monsters" type="banner" url="https://artworks.thetvdb.com/banners/v4/series/422598/banners/65870d3444ee9.jpg"/><Image alt="Monarch: Legacy of Monsters" type="clearLogo" url="https://metadata-static.plex.tv/8/683a142553/870248ff540f2173b811c2ad6c8adf30.png"/><Image alt="Monarch: Legacy of Monsters" type="clearLogoWide" url="https://metadata-static.plex.tv/8/683a142553/870248ff540f2173b811c2ad6c8adf30.png"/><Image alt="Monarch: Legacy of Monsters" type="coverArt" url="https://metadata-static.plex.tv/f/gracenote/f9bcfe89e8d3b057367fc1b5a9b7e60e.jpg"/><Image alt="Monarch: Legacy of Monsters" type="coverPoster" url="https://image.tmdb.org/t/p/original/uwrQHMnXD2DA1rvaMZk4pavZ3CY.jpg"/><Image alt="Monarch: Legacy of Monsters" type="coverSquare" url="https://metadata-static.plex.tv/1/gracenote/13d6951d694a79fc83131ee177e5ed0f.jpg"/></Directory></MediaContainer>
            """.trimIndent()
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
