package com.example.cineflow.data.model

data class PromptExample(
    val id: String,
    val title: String,
    val promptText: String,
    val genre: GenreCategory,
    val style: StylePreset,
    val complexity: ComplexityLevel,
    val cameraMotion: CameraMotion,
    val lighting: String,
    val languageCode: String = "en",
    val languageName: String = "English",
    val previewDrawableRes: Int? = null,
    val tags: List<String> = emptyList(),
    val directorNotes: String = ""
)
