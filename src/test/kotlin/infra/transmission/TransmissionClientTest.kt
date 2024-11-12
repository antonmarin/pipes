package ru.antonmarin.autoget.infra.transmission

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import ru.antonmarin.autoget.actions.contracts.Torrent
import ru.antonmarin.autoget.framework.DataFactory
import ru.antonmarin.autoget.framework.TransmissionDependent
import java.net.URI

class TransmissionClientTest : TransmissionDependent {
    private lateinit var client: TransmissionClient

    @BeforeEach
    fun resetServer() {
        client = TransmissionClient(TransmissionDependent.getApiUrl())
    }

    @Nested
    inner class TorrentAddTest {
        @Test
        fun `should response when added using magnet`() {
            val response = client.add(
                URI(DataFactory.MAGNET_URI),
                "/someDir"
            )

            Assertions.assertThat(response).isEqualTo(1L)
        }
    }

    @Nested
    inner class TorrentGetTest {
        @Test
        fun `should return existed torrent`() {
            val existedId = client.add(
                URI(DataFactory.MAGNET_URI),
                "/someDir"
            )

            val response = client.findOne(existedId)

            Assertions.assertThat(response).isInstanceOf(Torrent::class.java)
            Assertions.assertThat(response!!.id).isEqualTo(existedId)
        }
    }

    @Nested
    inner class TorrentRenamePathTest {
        @Test
        fun `should change path and name of torrent`() {
            val existedId = client.add(
                URI("https://github.com/antonmarin/pipes/raw/refs/heads/master/src/test/resources/ubuntu-mate-22.04.5-desktop-amd64.iso.torrent"),
                "/someDir"
            )
            val gotTorrent = client.findOne(existedId) ?: fail("Unexpected behavior")

            val response = client.rename(
                existedId,
                gotTorrent.title,
                "Somenew",
            )

            Assertions.assertThat(response).isTrue
        }
    }
}
