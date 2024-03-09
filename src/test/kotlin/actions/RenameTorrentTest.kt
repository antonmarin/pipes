package ru.antonmarin.autoget.actions

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.actions.contracts.TorrentClient
import ru.antonmarin.autoget.framework.DataFactory

class RenameTorrentTest {
    private val client = mockk<TorrentClient>()
    private val capturedId = slot<Long>()
    private val capturedFrom = slot<String>()
    private val capturedTo = slot<String>()

    @BeforeEach
    fun resetStubs() {
        capturedId.clear()
        capturedFrom.clear()
        capturedTo.clear()
        every { client.rename(capture(capturedId), capture(capturedFrom), capture(capturedTo)) } returns true
        every { client.findOne(any()) } answers { DataFactory.torrent(firstArg(), "Prev title", true) }
    }

    @Test
    fun `should rename torrent with torrentTitle when id and title filled and metadata presented`() {
        val action = RenameTorrent(client)

        val entry = DataFactory.entry(torrentId = 3, torrentTitle = "Some Title [1080p]")
        action.execute(listOf(entry))

        Assertions.assertThat(capturedTo.captured).isEqualTo(entry.torrentTitle)
    }

    @Test
    fun `should skip when no metadata`() {
        every { client.findOne(any()) } answers { DataFactory.torrent(firstArg(), "Prev title", false) }

        val action = RenameTorrent(client)

        val entry = DataFactory.entry(torrentId = 3, torrentTitle = "Some Title [1080p]")
        action.execute(listOf(entry))

        Assertions.assertThat(capturedId.isCaptured).isFalse
    }
}
