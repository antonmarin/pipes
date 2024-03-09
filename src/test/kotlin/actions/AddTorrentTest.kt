package ru.antonmarin.autoget.actions

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.actions.contracts.TorrentClient
import ru.antonmarin.autoget.framework.DataFactory
import java.net.URI

class AddTorrentTest {
    private val capturedUri = slot<URI>()
    private val capturedDownloadDir = slot<String>()
    private val client = mockk<TorrentClient>()
    private val action = AddTorrent(client)

    @BeforeEach
    fun resetStubs() {
        every { client.add(capture(capturedUri), any()) } returns 1
    }

    @Test
    fun `should add marneturi to transmission when filled`() {
        val sutEntry =
            DataFactory.entry(torrentUrl = URI(DataFactory.MAGNET_URI))

        action.execute(listOf(sutEntry))

        Assertions.assertThat(capturedUri.captured).isEqualTo(sutEntry.torrentUri!!)
    }

    @Test
    fun `should set download-dir when filled`() {
        val sutEntry = DataFactory.entry(torrentUrl = URI("file.torrent"))
        val downloadDir = "/video/series"
        every { client.add(capture(capturedUri), capture(capturedDownloadDir)) } returns 1

        val action = AddTorrent(client, downloadDir)
        action.execute(listOf(sutEntry))

        Assertions.assertThat(capturedUri.captured)
            .isEqualTo(sutEntry.torrentUri!!)
        Assertions.assertThat(capturedDownloadDir.captured)
            .isEqualTo(downloadDir)
    }

    @Test
    fun `should set id when success`() {
        val sutEntry =
            DataFactory.entry(torrentUrl = URI(DataFactory.MAGNET_URI))
        val sutId: Long = 4
        every { client.add(capture(capturedUri), any()) } returns sutId

        action.execute(listOf(sutEntry))

        Assertions.assertThat(sutEntry.torrentId).isEqualTo(sutId)
    }
}
