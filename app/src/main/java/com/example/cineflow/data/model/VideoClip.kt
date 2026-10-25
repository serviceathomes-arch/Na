package com.example.cineflow.data.model

data class VideoClip(
    val id: String,
    val title: String,
    val prompt: String,
    val enhancedPrompt: String,
    val genre: GenreCategory,
    val style: StylePreset,
    val cameraMotion: CameraMotion,
    val aspectRatio: AspectRatioOption,
    val resolution: ResolutionOption,
    val audioSoundscape: AudioSoundscape,
    val durationSeconds: Int = 6,
    val timestampMs: Long = System.currentTimeMillis(),
    val videoFilePath: String? = null,
    val previewDrawableRes: Int? = null,
    val isFavorite: Boolean = false,
    val directorBreakdown: DirectorBreakdown = DirectorBreakdown()
)

data class DirectorBreakdown(
    val lensOptics: String = "35mm Anamorphic f/1.4",
    val cameraRig: String = "Technocrane with gyro stabilizer",
    val colorGrading: String = "Teal & Amber Film LUT with subtle halation",
    val lightingSetup: String = "Volumetric backlight with soft rim fill",
    val particleAtmosphere: String = "Micro dust motes & subtle anamorphic lens streaks",
    val audioCue: String = "Low frequency orchestral crescendo"
)
