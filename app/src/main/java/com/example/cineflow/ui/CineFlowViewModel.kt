package com.example.cineflow.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.cineflow.ai.GeminiDirectorService
import com.example.cineflow.data.model.AspectRatioOption
import com.example.cineflow.data.model.AudioSoundscape
import com.example.cineflow.data.model.CameraMotion
import com.example.cineflow.data.model.ComplexityLevel
import com.example.cineflow.data.model.DirectorBreakdown
import com.example.cineflow.data.model.GenreCategory
import com.example.cineflow.data.model.PromptExample
import com.example.cineflow.data.model.PromptTemplate
import com.example.cineflow.data.model.ResolutionOption
import com.example.cineflow.data.model.StylePreset
import com.example.cineflow.data.model.VideoClip
import com.example.cineflow.data.repository.GeneratedPromptResult
import com.example.cineflow.data.repository.PromptLibraryRepository
import com.example.cineflow.data.repository.VideoRepository
import com.example.cineflow.engine.CinematicVideoEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

enum class AppScreen {
    STUDIO,
    LIBRARY,
    RANDOMIZER,
    PLAYER,
    PROJECTS
}

enum class LibraryTab {
    GENRES,
    STYLES,
    COMPLEXITY,
    TEMPLATES
}

data class StudioState(
    val promptText: String = "A futuristic cyberpunk metropolis at midnight with glowing holographic neon billboards, flying sleek maglev skycars between towering glass spires, volumetric teal and amber fog, photorealistic 8K IMAX quality.",
    val enhancedPrompt: String? = null,
    val selectedGenre: GenreCategory = GenreCategory.SCI_FI,
    val selectedStyle: StylePreset = StylePreset.CYBER_NOIR,
    val selectedCamera: CameraMotion = CameraMotion.PAN_HORIZONTAL,
    val selectedAspectRatio: AspectRatioOption = AspectRatioOption.WIDESCREEN_16_9,
    val selectedResolution: ResolutionOption = ResolutionOption.HD_1080P,
    val selectedAudio: AudioSoundscape = AudioSoundscape.CYBER_PULSE,
    val durationSeconds: Int = 6,
    val isEnhancingPrompt: Boolean = false,
    val isRendering: Boolean = false,
    val renderProgress: Float = 0f,
    val renderStatusMessage: String = "",
    val detectedLanguage: String = "English"
)

data class LibraryState(
    val selectedTab: LibraryTab = LibraryTab.GENRES,
    val searchQuery: String = "",
    val selectedGenreFilter: GenreCategory = GenreCategory.ALL,
    val selectedStyleFilter: StylePreset? = null,
    val selectedComplexityFilter: ComplexityLevel? = null,
    val activeTemplate: PromptTemplate? = null,
    val templateSelections: Map<String, String> = emptyMap()
)

data class RandomizerState(
    val currentPrompt: GeneratedPromptResult = PromptLibraryRepository.generateRandomPrompt(),
    val isRolling: Boolean = false,
    val preferredGenre: GenreCategory = GenreCategory.ALL,
    val preferredStyle: StylePreset? = null
)

data class PlayerState(
    val activeClip: VideoClip? = null,
    val isPlaying: Boolean = true,
    val currentProgress: Float = 0f,
    val playbackSpeed: Float = 1.0f,
    val isLooping: Boolean = true,
    val isMuted: Boolean = false,
    val isShowingDirectorNotes: Boolean = false,
    val activeAspectRatio: AspectRatioOption? = null
)

data class SocialPublishState(
    val isOpen: Boolean = false,
    val targetClip: VideoClip? = null,
    val selectedPlatform: com.example.cineflow.data.model.SocialPlatform = com.example.cineflow.data.model.SocialPlatform.ALL,
    val customCaption: String = ""
)

class CineFlowViewModel(
    private val repository: VideoRepository
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.STUDIO)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _studioState = MutableStateFlow(StudioState())
    val studioState: StateFlow<StudioState> = _studioState.asStateFlow()

    private val _libraryState = MutableStateFlow(LibraryState())
    val libraryState: StateFlow<LibraryState> = _libraryState.asStateFlow()

    private val _randomizerState = MutableStateFlow(RandomizerState())
    val randomizerState: StateFlow<RandomizerState> = _randomizerState.asStateFlow()

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _socialPublishState = MutableStateFlow(SocialPublishState())
    val socialPublishState: StateFlow<SocialPublishState> = _socialPublishState.asStateFlow()

    val savedClips: StateFlow<List<VideoClip>> = repository.allClips.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            repository.initializeSamplesIfNeeded()
            // Default player to first demo clip if present
            val initial = repository.getClipById("demo_clip_1") ?: PromptLibraryRepository.initialClips.firstOrNull()
            if (initial != null) {
                _playerState.value = _playerState.value.copy(activeClip = initial)
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // STUDIO ACTIONS
    fun updatePromptText(text: String) {
        _studioState.value = _studioState.value.copy(
            promptText = text,
            enhancedPrompt = null // reset enhanced if user changed prompt
        )
    }

    fun selectGenre(genre: GenreCategory) {
        _studioState.value = _studioState.value.copy(selectedGenre = genre)
    }

    fun selectStyle(style: StylePreset) {
        _studioState.value = _studioState.value.copy(selectedStyle = style)
    }

    fun selectCamera(camera: CameraMotion) {
        _studioState.value = _studioState.value.copy(selectedCamera = camera)
    }

    fun selectAspectRatio(ratio: AspectRatioOption) {
        _studioState.value = _studioState.value.copy(selectedAspectRatio = ratio)
    }

    fun selectResolution(res: ResolutionOption) {
        _studioState.value = _studioState.value.copy(selectedResolution = res)
    }

    fun selectAudio(audio: AudioSoundscape) {
        _studioState.value = _studioState.value.copy(selectedAudio = audio)
    }

    fun selectDuration(seconds: Int) {
        _studioState.value = _studioState.value.copy(durationSeconds = seconds)
    }

    fun enhancePrompt() {
        val currentPrompt = _studioState.value.promptText
        if (currentPrompt.isBlank()) return

        viewModelScope.launch {
            _studioState.value = _studioState.value.copy(isEnhancingPrompt = true)
            try {
                val result = GeminiDirectorService.enhancePrompt(
                    userPrompt = currentPrompt,
                    selectedGenre = _studioState.value.selectedGenre,
                    selectedStyle = _studioState.value.selectedStyle
                )
                _studioState.value = _studioState.value.copy(
                    enhancedPrompt = result.enhancedPrompt,
                    selectedCamera = result.suggestedCamera,
                    selectedStyle = result.suggestedStyle,
                    detectedLanguage = result.detectedLanguage,
                    isEnhancingPrompt = false
                )
            } catch (e: Exception) {
                _studioState.value = _studioState.value.copy(isEnhancingPrompt = false)
            }
        }
    }

    fun startRenderVideo(context: Context) {
        val current = _studioState.value
        val prompt = current.promptText.trim()
        if (prompt.isBlank()) return

        val clipId = UUID.randomUUID().toString()
        val title = prompt.split(" ").take(4).joinToString(" ").replaceFirstChar { it.uppercase() }

        viewModelScope.launch {
            _studioState.value = current.copy(
                isRendering = true,
                renderProgress = 0.05f,
                renderStatusMessage = "Analyzing cinematic composition & lighting..."
            )

            // Select matching base asset or procedural
            val baseRes = when (current.selectedGenre) {
                GenreCategory.SCI_FI, GenreCategory.CYBERPUNK -> R.drawable.img_hero_cinema
                GenreCategory.FANTASY -> R.drawable.img_fantasy_portal
                else -> null
            }

            val renderedFile = CinematicVideoEngine.renderMp4Video(
                context = context,
                clipId = clipId,
                title = title,
                baseDrawableRes = baseRes,
                style = current.selectedStyle,
                cameraMotion = current.selectedCamera,
                aspectRatio = current.selectedAspectRatio,
                resolution = current.selectedResolution,
                durationSeconds = current.durationSeconds,
                onProgress = { prog, status ->
                    _studioState.value = _studioState.value.copy(
                        renderProgress = prog,
                        renderStatusMessage = status
                    )
                }
            )

            val breakdown = DirectorBreakdown(
                lensOptics = when (current.selectedStyle) {
                    StylePreset.CINEMATIC_8K -> "Arri Master Prime 50mm f/1.2"
                    StylePreset.CYBER_NOIR -> "Cooke Anamorphic /i 40mm"
                    StylePreset.VINTAGE_35MM -> "Vintage Super Baltar 35mm"
                    else -> "Zeiss Supreme Prime 35mm T/1.5"
                },
                cameraRig = current.selectedCamera.displayName + " Rig",
                colorGrading = current.selectedStyle.promptModifier.take(30),
                lightingSetup = "High-definition volumetric lighting with custom color pass",
                particleAtmosphere = "Simulated high-density particles & optical bloom",
                audioCue = current.selectedAudio.description
            )

            val newClip = VideoClip(
                id = clipId,
                title = title,
                prompt = prompt,
                enhancedPrompt = current.enhancedPrompt ?: prompt,
                genre = current.selectedGenre,
                style = current.selectedStyle,
                cameraMotion = current.selectedCamera,
                aspectRatio = current.selectedAspectRatio,
                resolution = current.selectedResolution,
                audioSoundscape = current.selectedAudio,
                durationSeconds = current.durationSeconds,
                videoFilePath = renderedFile?.absolutePath,
                previewDrawableRes = baseRes,
                directorBreakdown = breakdown
            )

            repository.saveClip(newClip)

            _studioState.value = _studioState.value.copy(
                isRendering = false,
                renderProgress = 1.0f
            )

            // Navigate to player to enjoy the newly generated video!
            _playerState.value = _playerState.value.copy(
                activeClip = newClip,
                isPlaying = true,
                currentProgress = 0f
            )
            _currentScreen.value = AppScreen.PLAYER
        }
    }

    // LIBRARY ACTIONS
    fun setLibraryTab(tab: LibraryTab) {
        _libraryState.value = _libraryState.value.copy(selectedTab = tab)
    }

    fun updateSearchQuery(query: String) {
        _libraryState.value = _libraryState.value.copy(searchQuery = query)
    }

    fun filterByGenre(genre: GenreCategory) {
        _libraryState.value = _libraryState.value.copy(selectedGenreFilter = genre)
    }

    fun filterByStyle(style: StylePreset?) {
        _libraryState.value = _libraryState.value.copy(selectedStyleFilter = style)
    }

    fun filterByComplexity(complexity: ComplexityLevel?) {
        _libraryState.value = _libraryState.value.copy(selectedComplexityFilter = complexity)
    }

    fun selectTemplate(template: PromptTemplate) {
        val defaultSelections = template.slots.associate { it.key to it.defaultVal }
        _libraryState.value = _libraryState.value.copy(
            activeTemplate = template,
            templateSelections = defaultSelections
        )
    }

    fun updateTemplateSlotValue(slotKey: String, value: String) {
        val current = _libraryState.value.templateSelections.toMutableMap()
        current[slotKey] = value
        _libraryState.value = _libraryState.value.copy(templateSelections = current)
    }

    fun applyTemplateToStudio() {
        val template = _libraryState.value.activeTemplate ?: return
        val assembledPrompt = template.buildPrompt(_libraryState.value.templateSelections)
        _studioState.value = _studioState.value.copy(
            promptText = assembledPrompt,
            selectedGenre = template.genre,
            enhancedPrompt = null
        )
        _libraryState.value = _libraryState.value.copy(activeTemplate = null)
        _currentScreen.value = AppScreen.STUDIO
    }

    fun usePromptExample(example: PromptExample) {
        _studioState.value = _studioState.value.copy(
            promptText = example.promptText,
            selectedGenre = example.genre,
            selectedStyle = example.style,
            selectedCamera = example.cameraMotion,
            enhancedPrompt = null
        )
        _currentScreen.value = AppScreen.STUDIO
    }

    // RANDOMIZER ACTIONS
    fun rollRandomPrompt() {
        viewModelScope.launch {
            _randomizerState.value = _randomizerState.value.copy(isRolling = true)
            kotlinx.coroutines.delay(350)
            val generated = PromptLibraryRepository.generateRandomPrompt(
                preferredGenre = _randomizerState.value.preferredGenre,
                preferredStyle = _randomizerState.value.preferredStyle
            )
            _randomizerState.value = _randomizerState.value.copy(
                currentPrompt = generated,
                isRolling = false
            )
        }
    }

    fun setRandomizerGenre(genre: GenreCategory) {
        _randomizerState.value = _randomizerState.value.copy(preferredGenre = genre)
        rollRandomPrompt()
    }

    fun setRandomizerStyle(style: StylePreset?) {
        _randomizerState.value = _randomizerState.value.copy(preferredStyle = style)
        rollRandomPrompt()
    }

    fun applyRandomPromptToStudio() {
        val p = _randomizerState.value.currentPrompt
        _studioState.value = _studioState.value.copy(
            promptText = p.promptText,
            selectedGenre = p.genre,
            selectedStyle = p.style,
            selectedCamera = p.cameraMotion,
            enhancedPrompt = null
        )
        _currentScreen.value = AppScreen.STUDIO
    }

    // PLAYER ACTIONS
    fun openClipInPlayer(clip: VideoClip) {
        _playerState.value = _playerState.value.copy(
            activeClip = clip,
            isPlaying = true,
            currentProgress = 0f,
            activeAspectRatio = clip.aspectRatio
        )
        _currentScreen.value = AppScreen.PLAYER
    }

    fun togglePlayPause() {
        _playerState.value = _playerState.value.copy(isPlaying = !_playerState.value.isPlaying)
    }

    fun setPlayerProgress(progress: Float) {
        _playerState.value = _playerState.value.copy(currentProgress = progress.coerceIn(0f, 1f))
    }

    fun setPlaybackSpeed(speed: Float) {
        _playerState.value = _playerState.value.copy(playbackSpeed = speed)
    }

    fun toggleLoop() {
        _playerState.value = _playerState.value.copy(isLooping = !_playerState.value.isLooping)
    }

    fun toggleMute() {
        _playerState.value = _playerState.value.copy(isMuted = !_playerState.value.isMuted)
    }

    fun toggleDirectorNotes() {
        _playerState.value = _playerState.value.copy(
            isShowingDirectorNotes = !_playerState.value.isShowingDirectorNotes
        )
    }

    fun setPlayerAspectRatio(ratio: AspectRatioOption) {
        _playerState.value = _playerState.value.copy(activeAspectRatio = ratio)
    }

    fun deleteClip(clipId: String) {
        viewModelScope.launch {
            repository.deleteClip(clipId)
            if (_playerState.value.activeClip?.id == clipId) {
                val remaining = savedClips.value.filter { it.id != clipId }
                _playerState.value = _playerState.value.copy(activeClip = remaining.firstOrNull())
                if (remaining.isEmpty()) {
                    _currentScreen.value = AppScreen.STUDIO
                }
            }
        }
    }

    fun toggleFavorite(clipId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(clipId)
        }
    }

    fun shareClip(context: Context, clip: VideoClip) {
        try {
            val videoPath = clip.videoFilePath
            if (videoPath != null) {
                val file = File(videoPath)
                if (file.exists()) {
                    val uri: Uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "video/mp4"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        putExtra(Intent.EXTRA_SUBJECT, clip.title)
                        putExtra(Intent.EXTRA_TEXT, "🎬 Created with CineFlow AI:\n\"${clip.prompt}\"")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Video"))
                    return
                }
            }

            // Share text prompt if video file not yet on disk
            val textShareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, clip.title)
                putExtra(Intent.EXTRA_TEXT, "🎬 CineFlow Video Prompt:\n\"${clip.prompt}\"\n\nStyle: ${clip.style.displayName} | Camera: ${clip.cameraMotion.displayName}")
            }
            context.startActivity(Intent.createChooser(textShareIntent, "Share Prompt"))
        } catch (e: Exception) {
            Toast.makeText(context, "Could not share: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // SOCIAL MEDIA AUTO-PILOT & PUBLISHING METHODS
    fun openSocialPublishDialog(clip: VideoClip, platform: com.example.cineflow.data.model.SocialPlatform = com.example.cineflow.data.model.SocialPlatform.ALL) {
        val caption = generateSocialCaption(clip, platform)
        _socialPublishState.value = SocialPublishState(
            isOpen = true,
            targetClip = clip,
            selectedPlatform = platform,
            customCaption = caption
        )
    }

    fun closeSocialPublishDialog() {
        _socialPublishState.value = _socialPublishState.value.copy(isOpen = false)
    }

    fun selectPublishPlatform(platform: com.example.cineflow.data.model.SocialPlatform) {
        val clip = _socialPublishState.value.targetClip
        val newCaption = if (clip != null) generateSocialCaption(clip, platform) else ""
        _socialPublishState.value = _socialPublishState.value.copy(
            selectedPlatform = platform,
            customCaption = newCaption
        )
    }

    fun updateSocialCaption(caption: String) {
        _socialPublishState.value = _socialPublishState.value.copy(customCaption = caption)
    }

    fun generateSocialCaption(clip: VideoClip, platform: com.example.cineflow.data.model.SocialPlatform): String {
        return "🎬 \"${clip.title}\"\n\n${clip.prompt.take(120)}...\n\n${platform.hashtags}"
    }

    fun startAutoPilotGenerateAndPublish(
        context: Context,
        platform: com.example.cineflow.data.model.SocialPlatform = com.example.cineflow.data.model.SocialPlatform.ALL
    ) {
        viewModelScope.launch {
            val random = PromptLibraryRepository.generateRandomPrompt(
                preferredStyle = StylePreset.entries.random()
            )
            val ratio = platform.recommendedRatio

            _studioState.value = _studioState.value.copy(
                promptText = random.promptText,
                selectedGenre = random.genre,
                selectedStyle = random.style,
                selectedCamera = random.cameraMotion,
                selectedAspectRatio = ratio,
                isRendering = true,
                renderProgress = 0.05f,
                renderStatusMessage = "⚡ Auto-Pilot: Generating trending clip for ${platform.displayName}..."
            )

            val clipId = UUID.randomUUID().toString()
            val baseRes = when (random.genre) {
                GenreCategory.SCI_FI, GenreCategory.CYBERPUNK -> R.drawable.img_hero_cinema
                GenreCategory.FANTASY -> R.drawable.img_fantasy_portal
                else -> null
            }

            val file = CinematicVideoEngine.renderMp4Video(
                context = context,
                clipId = clipId,
                title = random.title,
                baseDrawableRes = baseRes,
                style = random.style,
                cameraMotion = random.cameraMotion,
                aspectRatio = ratio,
                resolution = ResolutionOption.HD_1080P,
                durationSeconds = 6,
                onProgress = { prog, status ->
                    _studioState.value = _studioState.value.copy(
                        renderProgress = prog,
                        renderStatusMessage = "⚡ Auto-Pilot: $status"
                    )
                }
            )

            val newClip = VideoClip(
                id = clipId,
                title = random.title,
                prompt = random.promptText,
                enhancedPrompt = random.promptText,
                genre = random.genre,
                style = random.style,
                cameraMotion = random.cameraMotion,
                aspectRatio = ratio,
                resolution = ResolutionOption.HD_1080P,
                audioSoundscape = AudioSoundscape.CINEMATIC_ORCHESTRAL,
                durationSeconds = 6,
                videoFilePath = file?.absolutePath,
                previewDrawableRes = baseRes
            )

            repository.saveClip(newClip)

            _studioState.value = _studioState.value.copy(
                isRendering = false,
                renderProgress = 1.0f
            )

            _playerState.value = _playerState.value.copy(
                activeClip = newClip,
                isPlaying = true
            )

            openSocialPublishDialog(newClip, platform)
        }
    }

    fun uploadToSocialMedia(
        context: Context,
        clip: VideoClip,
        platform: com.example.cineflow.data.model.SocialPlatform,
        customCaption: String
    ) {
        try {
            val videoPath = clip.videoFilePath
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                if (videoPath != null && File(videoPath).exists()) {
                    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", File(videoPath))
                    type = "video/mp4"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    type = "text/plain"
                }
                putExtra(Intent.EXTRA_SUBJECT, clip.title)
                putExtra(Intent.EXTRA_TEXT, customCaption)
            }

            if (platform.packageName != null) {
                sendIntent.setPackage(platform.packageName)
                try {
                    context.startActivity(sendIntent)
                    closeSocialPublishDialog()
                    return
                } catch (e: Exception) {
                    sendIntent.setPackage(null)
                }
            }

            val chooser = Intent.createChooser(sendIntent, "Upload to ${platform.displayName}")
            context.startActivity(chooser)
            closeSocialPublishDialog()
        } catch (e: Exception) {
            Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

