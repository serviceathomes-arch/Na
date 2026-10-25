package com.example.cineflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_clips")
data class ClipEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val prompt: String,
    val enhancedPrompt: String,
    val genreName: String,
    val styleName: String,
    val cameraMotionName: String,
    val aspectRatioName: String,
    val resolutionName: String,
    val audioSoundscapeName: String,
    val durationSeconds: Int,
    val timestampMs: Long,
    val videoFilePath: String?,
    val previewDrawableRes: Int?,
    val isFavorite: Boolean,
    val lensOptics: String,
    val cameraRig: String,
    val colorGrading: String,
    val lightingSetup: String,
    val particleAtmosphere: String,
    val audioCue: String
)
