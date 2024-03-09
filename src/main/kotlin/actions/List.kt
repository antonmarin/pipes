package ru.antonmarin.autoget.actions

import ru.antonmarin.autoget.Action
import ru.antonmarin.autoget.Entry
import kotlin.collections.List

class List(private val list: List<Entry>) : Action {
    override fun execute(entries: List<Entry>): List<Entry> = list
}
