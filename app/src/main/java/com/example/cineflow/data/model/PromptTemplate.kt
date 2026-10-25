package com.example.cineflow.data.model

data class TemplateSlot(
    val key: String,
    val label: String,
    val defaultVal: String,
    val suggestedOptions: List<String>
)

data class PromptTemplate(
    val id: String,
    val title: String,
    val description: String,
    val genre: GenreCategory,
    val templatePattern: String, // e.g. "A {camera} of {subject} in {setting}, illuminated by {lighting}, rendered in {style} style."
    val slots: List<TemplateSlot>
) {
    fun buildPrompt(selectedValues: Map<String, String>): String {
        var result = templatePattern
        slots.forEach { slot ->
            val value = selectedValues[slot.key]?.ifBlank { slot.defaultVal } ?: slot.defaultVal
            result = result.replace("{${slot.key}}", value)
        }
        return result
    }
}
