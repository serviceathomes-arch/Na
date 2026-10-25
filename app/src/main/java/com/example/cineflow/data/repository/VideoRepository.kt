package com.example.cineflow.data.repository

import com.example.cineflow.data.local.CineDatabase
import com.example.cineflow.data.local.ClipEntity
import com.example.cineflow.data.model.AspectRatioOption
import com.example.cineflow.data.model.AudioSoundscape
import com.example.cineflow.data.model.CameraMotion
import com.example.cineflow.data.model.DirectorBreakdown
import com.example.cineflow.data.model.GenreCategory
import com.example.cineflow.data.model.ResolutionOption
import com.example.cineflow.data.model.StylePreset
import com.example.cineflow.data.model.VideoClip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VideoRepository(private val database: CineDatabase) {

    private val clipDao = database.clipDao()

    val allClips: Flow<List<VideoClip>> = clipDao.getAllClips().map { entities ->
        entities.map { it.toDomain() }
    }

    val favoriteClips: Flow<List<VideoClip>> = clipDao.getFavoriteClips().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun getClipById(id: String): VideoClip? {
        return clipDao.getClipById(id)?.toDomain()
    }

    suspend fun saveClip(clip: VideoClip) {
        clipDao.insertClip(clip.toEntity())
    }

    suspend fun deleteClip(clipId: String) {
        clipDao.deleteClipById(clipId)
    }

    suspend fun toggleFavorite(clipId: String) {
        val existing = clipDao.getClipById(clipId) ?: return
        clipDao.updateClip(existing.copy(isFavorite = !existing.isFavorite))
    }

    suspend fun initializeSamplesIfNeeded() {
        // Pre-populate demo clips if empty so user has immediate rich preview
        val first = clipDao.getClipById("demo_clip_1")
        if (first == null) {
            PromptLibraryRepository.initialClips.forEach { clip ->
                clipDao.insertClip(clip.toEntity())
            }
        }
    }

    private fun ClipEntity.toDomain(): VideoClip {
        return VideoClip(
            id = id,
            title = title,
            prompt = prompt,
            enhancedPrompt = enhancedPrompt,
            genre = try { GenreCategory.valueOf(genreName) } catch (e: Exception) { GenreCategory.SCI_FI },
            style = try { StylePreset.valueOf(styleName) } catch (e: Exception) { StylePreset.CINEMATIC_8K },
            cameraMotion = try { CameraMotion.valueOf(cameraMotionName) } catch (e: Exception) { CameraMotion.DOLLY_PUSH },
            aspectRatio = try { AspectRatioOption.valueOf(aspectRatioName) } catch (e: Exception) { AspectRatioOption.WIDESCREEN_16_9 },
            resolution = try { ResolutionOption.valueOf(resolutionName) } catch (e: Exception) { ResolutionOption.HD_1080P },
            audioSoundscape = try { AudioSoundscape.valueOf(audioSoundscapeName) } catch (e: Exception) { AudioSoundscape.CINEMATIC_ORCHESTRAL },
            durationSeconds = durationSeconds,
            timestampMs = timestampMs,
            videoFilePath = videoFilePath,
            previewDrawableRes = previewDrawableRes,
            isFavorite = isFavorite,
            directorBreakdown = DirectorBreakdown(
                lensOptics = lensOptics,
                cameraRig = cameraRig,
                colorGrading = colorGrading,
                lightingSetup = lightingSetup,
                particleAtmosphere = particleAtmosphere,
                audioCue = audioCue
            )
        )
    }

    private fun VideoClip.toEntity(): ClipEntity {
        return ClipEntity(
            id = id,
            title = title,
            prompt = prompt,
            enhancedPrompt = enhancedPrompt,
            genreName = genre.name,
            styleName = style.name,
            cameraMotionName = cameraMotion.name,
            aspectRatioName = aspectRatio.name,
            resolutionName = resolution.name,
            audioSoundscapeName = audioSoundscape.name,
            durationSeconds = durationSeconds,
            timestampMs = timestampMs,
            videoFilePath = videoFilePath,
            previewDrawableRes = previewDrawableRes,
            isFavorite = isFavorite,
            lensOptics = directorBreakdown.lensOptics,
            cameraRig = directorBreakdown.cameraRig,
            colorGrading = directorBreakdown.colorGrading,
            lightingSetup = directorBreakdown.lightingSetup,
            particleAtmosphere = directorBreakdown.particleAtmosphere,
            audioCue = directorBreakdown.audioCue
        )
    }
}
