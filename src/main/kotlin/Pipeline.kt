package ru.antonmarin.autoget

import org.slf4j.LoggerFactory
import java.net.URI

class Pipeline(private val actions: Collection<Action>) {
    fun execute() {
        var previousEntries = emptyList<Entry>()
        actions.map {
            logger.trace("Executing action {} with entries {}", it, previousEntries)
            previousEntries = it.execute(previousEntries)
            logger.trace("Action {} finished with entries {}", it, previousEntries)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Pipeline::class.java)
    }
}

interface Action {
    fun execute(entries: List<Entry>): List<Entry>
}

data class Entry(
    var title: String? = null,
    var description: String? = null,
    var torrentUri: URI? = null,
    var videoResolution: String? = null,
    var torrentId: Long? = null,
    var torrentTitle: String? = null,
)
