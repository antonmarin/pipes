package ru.antonmarin.autoget.actions

import ru.antonmarin.autoget.Action
import ru.antonmarin.autoget.Entry
import ru.antonmarin.autoget.Template
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.collections.List

// Broken!! torrent adds as original
class RenameMagnetURI(template: String) : Action {
    private val template = Template(template)
    override fun execute(entries: List<Entry>): List<Entry> {
        entries.forEach { entry ->
            val torrentUri = entry.torrentUri ?: return@forEach
            entry.torrentUri = torrentUri.schemeSpecificPart.trimStart('?').split('&').map {
                if (it.startsWith("dn=")) {
                    "dn="+urlEncode(template.replace(template.prepareTokens(entry)))
                } else if (it.startsWith("tr=")) {
                    it.removePrefix("tr=").let { tr -> "tr=" + urlEncode(tr) }
                } else {
                    it
                }
            }.joinToString("&")
                .let { URI("${torrentUri.scheme}:?$it") }
        }
        return entries
    }

    private fun urlEncode(string: String?): String? = URLEncoder.encode(string, UTF_8)
}
