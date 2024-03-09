package ru.antonmarin.autoget.actions

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import ru.antonmarin.autoget.framework.DataFactory
import java.net.URI

class RenameMagnetURITest {
    @Test
    fun `should replace with title when templated by title`() {
        val entry = DataFactory.entry(title = "Название", torrentUrl = URI("magnet:?xt=urn:btih:f6ae530cb7a93ec8bafc647a6b6b94564c06b0d9&dn=Momochi-san+Chi+no+Ayakashi+Ouji+%5BWEBRip+1080p%5D&xl=15057857801&tr=http%3A%2F%2Ftr.asdf.fun%3A2710%2Fannounce&tr=http%3A%2F%2Fretracker.local%2Fannounce"))

        val action = RenameMagnetURI(template = "{title}")
        action.execute(listOf(entry))

        Assertions.assertThat(entry.torrentUri).isEqualTo(URI("magnet:?xt=urn:btih:f6ae530cb7a93ec8bafc647a6b6b94564c06b0d9&dn=%D0%9D%D0%B0%D0%B7%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5&xl=15057857801&tr=http%3A%2F%2Ftr.asdf.fun%3A2710%2Fannounce&tr=http%3A%2F%2Fretracker.local%2Fannounce"))
    }
}
