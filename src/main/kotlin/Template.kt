package ru.antonmarin.autoget

class Template(private val template: String) {
    fun replace(tokens: Map<String, String>): String {
        return replaceTokens(template, tokens)
    }

    private fun replaceTokens(string: String, map: Map<String, String?>): String =
        map.entries.fold(string) { acc, (key, value) ->
            if (value != null) {
                acc.replace("{$key}", value)
            } else {
                acc
            }
        }

    fun prepareTokens(entry: Entry): Map<String, String> = mapOf(
        "title" to (entry.title ?: ""),
        "videoResolution" to (entry.videoResolution ?: ""),
    )
}
fun Regex.findFirstGroup(string: String): String? = this.find(string)?.groupValues?.get(1)
