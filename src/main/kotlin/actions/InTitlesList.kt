package ru.antonmarin.autoget.actions

import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.Action
import ru.antonmarin.autoget.Entry
import kotlin.collections.List

class InTitlesList(private val titlesProvider: TitlesProvider) : Action {
    override fun execute(entries: List<Entry>): List<Entry> {
        val titles = titlesProvider.getTitles()
        logger.trace("filtering entries by titles {}", titles)
        val filtered = entries.filter { entry ->
            val title = entry.title ?: return@filter false
            titles.any { title.contains(it) }
        }

        return filtered
    }

    companion object {
        private val logger = LoggerFactory.getLogger(InTitlesList::class.java)
    }
}

interface TitlesProvider {
    fun getTitles(): List<String>
}

class FixedList(private val list: List<String>) : TitlesProvider {
    override fun getTitles(): List<String> = list
}

class AddAkasList(
    private val akasProvider: AkasProvider,
    private val input: TitlesProvider,
) : TitlesProvider {
    override fun getTitles(): List<String> = input.getTitles()
        .also { logger.trace("Adding akas to titles: {}", it) }
        .flatMap {
            val akas = akasProvider.getAkas(it)
            logger.debug("found ${akas.size} akas for $it")
            akas
        } + input.getTitles()

    companion object {
        private val logger = LoggerFactory.getLogger(AddAkasList::class.java)
    }
}

interface AkasProvider {
    fun getAkas(input: String): List<String>
}
