package com.example.cineflow.ai

import android.util.Log
import com.example.BuildConfig
import com.example.cineflow.data.model.CameraMotion
import com.example.cineflow.data.model.DirectorBreakdown
import com.example.cineflow.data.model.GenreCategory
import com.example.cineflow.data.model.StylePreset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

private const val TAG = "GeminiDirectorService"

data class DirectorEnhancementResult(
    val enhancedPrompt: String,
    val suggestedCamera: CameraMotion,
    val suggestedStyle: StylePreset,
    val detectedLanguage: String,
    val directorBreakdown: DirectorBreakdown
)

object GeminiDirectorService {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun enhancePrompt(
        userPrompt: String,
        selectedGenre: GenreCategory,
        selectedStyle: StylePreset
    ): DirectorEnhancementResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val remoteResult = callGeminiApi(apiKey, userPrompt, selectedGenre, selectedStyle)
                if (remoteResult != null) return@withContext remoteResult
            } catch (e: Exception) {
                Log.w(TAG, "Gemini API call failed, using intelligent on-device director: ${e.message}")
            }
        }

        // Intelligent On-Device Cinematic Director
        generateOnDeviceEnhancement(userPrompt, selectedGenre, selectedStyle)
    }

    private fun callGeminiApi(
        apiKey: String,
        userPrompt: String,
        genre: GenreCategory,
        style: StylePreset
    ): DirectorEnhancementResult? {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val systemInstruction = """
            You are an elite cinematic film director and visual effects supervisor for an AI video generator.
            Given a user's prompt in any language, expand it into a detailed cinematic prompt optimized for high-definition video synthesis.
            Return a JSON object with:
            {
              "enhancedPrompt": "concise 3-sentence director's script with camera motion, lighting, and textures",
              "cameraMotion": "DOLLY_PUSH|PAN_HORIZONTAL|DRONE_CRANE|ORBIT_360|TILT_UP|SLOW_MO_120FPS|DYNAMIC_HANDHELD",
              "lensOptics": "e.g. 35mm Anamorphic f/1.4",
              "cameraRig": "e.g. Technocrane with gyro stabilizer",
              "colorGrading": "e.g. Teal & Amber film LUT",
              "lightingSetup": "e.g. Volumetric backlight with rim fill",
              "particleAtmosphere": "e.g. Micro rain droplets catching neon lights",
              "audioCue": "e.g. Swelling orchestral strings with deep synth bass",
              "detectedLanguage": "English or detected language name"
            }
        """.trimIndent()

        val jsonPayload = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", "$systemInstruction\n\nUser Prompt: $userPrompt\nPreferred Genre: ${genre.displayName}\nPreferred Style: ${style.displayName}"))
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json")
            })
        }

        val request = Request.Builder()
            .url(url)
            .post(jsonPayload.toString().toRequestBody("application/json".toMediaType()))
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) return null

        val responseBody = response.body?.string() ?: return null
        val root = JSONObject(responseBody)
        val candidates = root.optJSONArray("candidates") ?: return null
        if (candidates.length() == 0) return null
        val text = candidates.getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text")

        val parsed = JSONObject(text)
        val motionStr = parsed.optString("cameraMotion", "DOLLY_PUSH")
        val motion = try {
            CameraMotion.valueOf(motionStr)
        } catch (e: Exception) {
            CameraMotion.DOLLY_PUSH
        }

        return DirectorEnhancementResult(
            enhancedPrompt = parsed.optString("enhancedPrompt", userPrompt),
            suggestedCamera = motion,
            suggestedStyle = style,
            detectedLanguage = parsed.optString("detectedLanguage", "English"),
            directorBreakdown = DirectorBreakdown(
                lensOptics = parsed.optString("lensOptics", "35mm Anamorphic f/1.4"),
                cameraRig = parsed.optString("cameraRig", "Steadicam on 20m rail"),
                colorGrading = parsed.optString("colorGrading", "Cinema Film Print LUT"),
                lightingSetup = parsed.optString("lightingSetup", "Volumetric key with warm ambient bounce"),
                particleAtmosphere = parsed.optString("particleAtmosphere", "Atmospheric haze & dust motes"),
                audioCue = parsed.optString("audioCue", "Cinematic orchestral score")
            )
        )
    }

    private fun generateOnDeviceEnhancement(
        userPrompt: String,
        genre: GenreCategory,
        style: StylePreset
    ): DirectorEnhancementResult {
        val trimmed = userPrompt.trim()
        val detectedLanguage = detectLanguage(trimmed)

        val camera = when (genre) {
            GenreCategory.ACTION_THRILLER -> CameraMotion.DYNAMIC_HANDHELD
            GenreCategory.SCI_FI -> CameraMotion.PAN_HORIZONTAL
            GenreCategory.FANTASY -> CameraMotion.DRONE_CRANE
            GenreCategory.ROMANCE -> CameraMotion.DOLLY_PUSH
            GenreCategory.NATURE_WILDLIFE -> CameraMotion.DRONE_CRANE
            else -> CameraMotion.DOLLY_PUSH
        }

        val lens = when (style) {
            StylePreset.CINEMATIC_8K -> "Arri Master Prime 50mm f/1.2"
            StylePreset.CYBER_NOIR -> "Cooke Anamorphic /i Full Frame Plus 40mm"
            StylePreset.VINTAGE_35MM -> "Super Baltar 35mm Vintage Cine Lens"
            StylePreset.AERIAL_IMAX -> "Ultra-Wide 18mm IMAX Hasselblad Lens"
            else -> "Zeiss Supreme Prime 35mm T/1.5"
        }

        val rig = when (camera) {
            CameraMotion.DRONE_CRANE -> "Heavy-lift cinema drone with brushless gimbal"
            CameraMotion.DOLLY_PUSH -> "Precision motorized track dolly"
            CameraMotion.DYNAMIC_HANDHELD -> "Active 3-axis gyro rig with organic human tracking"
            CameraMotion.ORBIT_360 -> "High-speed robotic motion control arm (KUKA)"
            else -> "Hydraulic jib crane on smooth rail"
        }

        val lighting = when (style) {
            StylePreset.CYBER_NOIR -> "Cyan neon edge light with deep magenta fill and wet asphalt reflections"
            StylePreset.DARK_FANTASY -> "Moody chiaroscuro with glowing ember highlights and low key fill"
            StylePreset.ANIME_SHINKAI -> "High-contrast golden twilight sunbeams with painterly sky scattering"
            StylePreset.VINTAGE_35MM -> "Warm tungsten lanterns with soft diffusion halation"
            else -> "Volumetric golden hour sunbeams with soft atmospheric haze"
        }

        val colorLUT = when (style) {
            StylePreset.CYBER_NOIR -> "Cyber Neo-Noir: Teal shadows, punchy magenta highlights, crushed blacks"
            StylePreset.VINTAGE_35MM -> "Kodachrome 64 Print Emulation: Rich warm tones, organic film roll-off"
            StylePreset.DARK_FANTASY -> "Bleach Bypass / Dark Fantasy: Desaturated midtones, crimson accents"
            StylePreset.ANIME_SHINKAI -> "Shinkai Vivid: Vibrant cerulean blues, radiant apricot sunsets"
            else -> "Kodak 2383 Film Print LUT: Natural skin tones, filmic contrast"
        }

        val particle = when (style) {
            StylePreset.CYBER_NOIR -> "Fine rain drizzle catching neon glow with street steam"
            StylePreset.DARK_FANTASY -> "Drifting charcoal embers and delicate ash plumes"
            StylePreset.ANIME_SHINKAI -> "Luminous celestial light specks and floating petals"
            else -> "Subtle golden dust motes dancing in sunbeams"
        }

        val audio = when (genre) {
            GenreCategory.SCI_FI, GenreCategory.CYBERPUNK -> "Sub-bass synth pulse with electronic shimmers"
            GenreCategory.ROMANCE -> "Gentle solo cello and warm piano chords"
            GenreCategory.ACTION_THRILLER -> "Heartbeat percussion crescendo with rising tension"
            GenreCategory.NATURE_WILDLIFE -> "Forest wind ambience with distant water streams"
            else -> "Sweeping cinematic strings and brass crescendo"
        }

        val enhanced = "An awe-inspiring cinematic masterwork of $trimmed. Captured with ${camera.promptDescription}. Featuring $lighting and $particle. Rendered in ${style.promptModifier}."

        return DirectorEnhancementResult(
            enhancedPrompt = enhanced,
            suggestedCamera = camera,
            suggestedStyle = style,
            detectedLanguage = detectedLanguage,
            directorBreakdown = DirectorBreakdown(
                lensOptics = lens,
                cameraRig = rig,
                colorGrading = colorLUT,
                lightingSetup = lighting,
                particleAtmosphere = particle,
                audioCue = audio
            )
        )
    }

    private fun detectLanguage(text: String): String {
        return when {
            text.any { it in '\u3040'..'\u30ff' || it in '\u4e00'..'\u9faf' } -> "Japanese / Chinese"
            text.any { it in '\u0900'..'\u097F' } -> "Hindi"
            text.any { it in '\u0600'..'\u06FF' } -> "Arabic"
            text.any { it in '\u0400'..'\u04FF' } -> "Cyrillic"
            text.contains(" de ") || text.contains(" un ") || text.contains(" el ") || text.contains(" la ") || text.contains(" con ") -> "Spanish / Romance"
            text.contains(" le ") || text.contains(" les ") || text.contains(" dans ") || text.contains(" avec ") -> "French"
            text.contains(" der ") || text.contains(" die ") || text.contains(" das ") || text.contains(" und ") -> "German"
            else -> "English"
        }
    }
}
