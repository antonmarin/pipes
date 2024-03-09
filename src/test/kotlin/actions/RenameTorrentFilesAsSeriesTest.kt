package ru.antonmarin.autoget.actions

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.Template
import ru.antonmarin.autoget.actions.contracts.Torrent
import ru.antonmarin.autoget.actions.contracts.TorrentClient
import ru.antonmarin.autoget.actions.contracts.TorrentFile
import ru.antonmarin.autoget.framework.DataFactory

class RenameTorrentFilesAsSeriesTest {
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
    fun `should rename files when meta ready and episode match Regex`() {
        val file = TorrentFile("title [1080p]/some_[03]_smthElse_here.mkv")
        val torrentId = 3L
        every { client.findOne(any()) } returns Torrent(
            torrentId, "null", true, listOf(file),
        )

        val entry = DataFactory.entry(torrentId = torrentId)
        val action = RenameTorrentFilesAsSeries(
            client,
            Regex(".*/[^\\[]+\\[(\\d+)"),
            Template("s01e{episodeNumber}.mkv")
        )
        action.execute(listOf(entry))

        Assertions.assertThat(capturedId.captured)
            .isEqualTo(torrentId)
        Assertions.assertThat(capturedFrom.captured)
            .isEqualTo(file.name)
        Assertions.assertThat(capturedTo.captured)
            .isEqualTo("s01e03.mkv")
    }
}
