package ru.antonmarin.autoget.actions

import ru.antonmarin.autoget.Action
import ru.antonmarin.autoget.Entry
import ru.antonmarin.autoget.Template
import ru.antonmarin.autoget.findFirstGroup
import kotlin.collections.List
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

class Modify(
    private val targetProperty: KMutableProperty1<Entry, String?>,
    private val modifySource: ModifySource,
) : Action {
    constructor(
        targetProperty: KMutableProperty1<Entry, String?>,
        sourceProperty: KProperty1<Entry, String?>,
        firstGroupRegex: Regex?
    ) : this(targetProperty, PropertyRegex(sourceProperty, firstGroupRegex))
    constructor(
        targetProperty: KMutableProperty1<Entry, String?>,
        template: String
    ) : this(targetProperty, ModifyTemplate(template))

    override fun execute(entries: List<Entry>): List<Entry> {
        entries.forEach { entry ->
            val targetValue = modifySource.get(entry) ?: return@forEach

            targetProperty.set(entry, targetValue)
        }

        return entries
    }

    interface ModifySource {
        fun get(entry: Entry): String?
    }

    class ModifyTemplate(template: String) : ModifySource {
        private val template = Template(template)

        override fun get(entry: Entry): String {
            return template.replace(template.prepareTokens(entry))
        }
    }

    class PropertyRegex(
        private val sourceProperty: KProperty1<Entry, String?>,
        private val firstGroupRegex: Regex?,
        ) : ModifySource {
        override fun get(entry: Entry): String? {
            val value = sourceProperty.get(entry) ?: return null
            return if (firstGroupRegex is Regex) {
                firstGroupRegex.findFirstGroup(value)
            } else {
                value
            }
        }
    }
}
