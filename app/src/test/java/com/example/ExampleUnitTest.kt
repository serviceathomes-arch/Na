package com.example

import com.example.cineflow.data.model.DurationOption
import com.example.cineflow.data.model.GenreCategory
import com.example.cineflow.data.model.StylePreset
import com.example.cineflow.data.repository.PromptLibraryRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testRandomPromptGeneratorProducesValidPrompt() {
        val result = PromptLibraryRepository.generateRandomPrompt(
            preferredGenre = GenreCategory.SCI_FI,
            preferredStyle = StylePreset.CINEMATIC_8K
        )
        assertNotNull(result)
        assertTrue(result.promptText.isNotBlank())
        assertTrue(result.title.isNotBlank())
        assertEquals(GenreCategory.SCI_FI, result.genre)
        assertEquals(StylePreset.CINEMATIC_8K, result.style)
        assertNotNull(result.cameraMotion)
    }

    @Test
    fun testPromptTemplateInterpolation() {
        val template = PromptLibraryRepository.promptTemplates.first()
        val selections = mapOf(
            "camera_motion" to "slow dolly push",
            "subject" to "a crystal monolith",
            "setting" to "a misty valley",
            "lighting" to "golden sunset rays",
            "style_preset" to "8K cinema",
            "atmosphere" to "floating embers"
        )
        val prompt = template.buildPrompt(selections)
        assertTrue(prompt.contains("crystal monolith"))
        assertTrue(prompt.contains("misty valley"))
        assertFalse(prompt.contains("{subject}"))
    }

    @Test
    fun testDurationOptionsIncludesTenMinutes() {
        val tenMin = DurationOption.MIN_10
        assertEquals(600, tenMin.seconds)
        assertTrue(tenMin.isEpic)
        assertEquals("10 Min", tenMin.label)
    }

    @Test
    fun testPromptLibraryContainsMultilingualAndCategorizedEntries() {
        val examples = PromptLibraryRepository.promptExamples
        assertTrue(examples.size >= 10)
        // Verify Spanish, Japanese, French, German entries exist
        assertTrue(examples.any { it.languageCode == "es" })
        assertTrue(examples.any { it.languageCode == "ja" })
        assertTrue(examples.any { it.languageCode == "fr" })
        assertTrue(examples.any { it.languageCode == "de" })
    }
}
