package ru.antonmarin.autoget.actions

import org.slf4j.LoggerFactory
import ru.antonmarin.autoget.Action
import ru.antonmarin.autoget.Entry
import kotlin.collections.List
import kotlin.reflect.KProperty1

class RegExp(
    val action: Action = Action.ACCEPT,
    val property: KProperty1<Entry, String?>,
    val anyExpr: List<Regex>,
) : Action {
    override fun execute(entries: List<Entry>): List<Entry> {
        return when (action) {
            Action.ACCEPT -> entries.filter {
                val value = property.getValue(it, property) ?: return@filter false

                anyExpr.any { regex -> regex.containsMatchIn(value) }
            }

            Action.REJECT -> entries.filterNot {
                val value = property.getValue(it, property) ?: return@filterNot false

                anyExpr.any { regex -> regex.containsMatchIn(value) }
            }
        }
    }

    enum class Action {
        ACCEPT,
        REJECT,
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RegExp::class.java)
    }
}
