package com.example.cineflow.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.os.Environment
import android.util.Log
import com.example.cineflow.data.model.AspectRatioOption
import com.example.cineflow.data.model.CameraMotion
import com.example.cineflow.data.model.ResolutionOption
import com.example.cineflow.data.model.StylePreset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.cos
import kotlin.math.sin

private const val TAG = "CinematicVideoEngine"

object CinematicVideoEngine {

    /**
     * Renders a real playable MP4 video file encoded with H.264 video codec!
     */
    suspend fun renderMp4Video(
        context: Context,
        clipId: String,
        title: String,
        baseDrawableRes: Int?,
        style: StylePreset,
        cameraMotion: CameraMotion,
        aspectRatio: AspectRatioOption,
        resolution: ResolutionOption,
        durationSeconds: Int = 5,
        onProgress: (Float, String) -> Unit
    ): File? = withContext(Dispatchers.IO) {
        try {
            val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                ?: context.filesDir
            if (!outputDir.exists()) outputDir.mkdirs()

            val sanitizedTitle = title.replace("[^a-zA-Z0-9_-]".toRegex(), "_").take(24)
            val outputFile = File(outputDir, "CineFlow_${sanitizedTitle}_${clipId.take(8)}.mp4")

            onProgress(0.10f, "Planning camera trajectory & lighting curves...")

            // Resolution dimensions (use multiples of 16 for H.264 codec compatibility)
            val width: Int
            val height: Int
            when (aspectRatio) {
                AspectRatioOption.VERTICAL_9_16 -> {
                    width = 720
                    height = 1280
                }
                AspectRatioOption.ANAMORPHIC_2_39 -> {
                    width = 1280
                    height = 544
                }
                AspectRatioOption.SQUARE_1_1 -> {
                    width = 720
                    height = 720
                }
                AspectRatioOption.CLASSIC_4_3 -> {
                    width = 960
                    height = 720
                }
                else -> { // 16:9
                    width = 1280
                    height = 720
                }
            }

            val fps = 30
            val totalFrames = durationSeconds * fps
            val bitRate = 4_000_000 // 4 Mbps high definition

            // Load base bitmap if resource provided, or synthesize
            val baseBitmap: Bitmap = if (baseDrawableRes != null) {
                try {
                    val decoded = BitmapFactory.decodeResource(context.resources, baseDrawableRes)
                    if (decoded != null) {
                        Bitmap.createScaledBitmap(decoded, width, height, true)
                    } else {
                        createSynthesizedBaseBitmap(width, height, style)
                    }
                } catch (e: Exception) {
                    createSynthesizedBaseBitmap(width, height, style)
                }
            } else {
                createSynthesizedBaseBitmap(width, height, style)
            }

            onProgress(0.25f, "Configuring H.264 high-definition video encoder...")

            val mimeType = "video/avc"
            val format = MediaFormat.createVideoFormat(mimeType, width, height).apply {
                setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
                setInteger(MediaFormat.KEY_BIT_RATE, bitRate)
                setInteger(MediaFormat.KEY_FRAME_RATE, fps)
                setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1) // 1 second keyframe interval
            }

            val codec = MediaCodec.createEncoderByType(mimeType)
            codec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            val inputSurface = codec.createInputSurface()
            codec.start()

            val muxer = MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            var trackIndex = -1
            var muxerStarted = false

            val bufferInfo = MediaCodec.BufferInfo()
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
            val particlePaint = Paint(Paint.ANTI_ALIAS_FLAG)

            onProgress(0.40f, "Rendering cinematic camera motion & particle dynamics...")

            for (frame in 0 until totalFrames) {
                val progress = frame.toFloat() / totalFrames.toFloat()

                // Lock hardware canvas from codec input surface
                val canvas: Canvas = inputSurface.lockHardwareCanvas()

                // Render dynamic frame with camera motion and particles
                renderCinematicFrame(
                    canvas = canvas,
                    width = width,
                    height = height,
                    baseBitmap = baseBitmap,
                    progress = progress,
                    cameraMotion = cameraMotion,
                    style = style,
                    paint = paint,
                    particlePaint = particlePaint
                )

                inputSurface.unlockCanvasAndPost(canvas)

                // Drain encoder
                while (true) {
                    val outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 0)
                    if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                        if (muxerStarted) {
                            throw RuntimeException("Format changed after muxer started")
                        }
                        val newFormat = codec.outputFormat
                        trackIndex = muxer.addTrack(newFormat)
                        muxer.start()
                        muxerStarted = true
                    } else if (outputBufferIndex >= 0) {
                        val encodedData = codec.getOutputBuffer(outputBufferIndex)
                        if (encodedData != null && (bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == 0 && bufferInfo.size != 0) {
                            if (!muxerStarted) {
                                throw RuntimeException("Muxer has not started yet")
                            }
                            encodedData.position(bufferInfo.offset)
                            encodedData.limit(bufferInfo.offset + bufferInfo.size)
                            muxer.writeSampleData(trackIndex, encodedData, bufferInfo)
                        }
                        codec.releaseOutputBuffer(outputBufferIndex, false)
                    } else {
                        break
                    }
                }

                if (frame % 15 == 0) {
                    val renderPercent = 0.40f + (progress * 0.45f)
                    val percentInt = (renderPercent * 100).toInt()
                    onProgress(renderPercent, "Synthesizing frame $frame / $totalFrames ($percentInt%)...")
                }
            }

            codec.signalEndOfInputStream()
            onProgress(0.90f, "Finalizing MP4 container & color metadata...")

            // Drain remaining frames until EOS
            var eos = false
            while (!eos) {
                val outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 10000)
                if (outputBufferIndex >= 0) {
                    val encodedData = codec.getOutputBuffer(outputBufferIndex)
                    if (encodedData != null && (bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) == 0 && bufferInfo.size != 0) {
                        if (muxerStarted) {
                            encodedData.position(bufferInfo.offset)
                            encodedData.limit(bufferInfo.offset + bufferInfo.size)
                            muxer.writeSampleData(trackIndex, encodedData, bufferInfo)
                        }
                    }
                    codec.releaseOutputBuffer(outputBufferIndex, false)
                    if ((bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        eos = true
                    }
                } else if (outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    eos = true
                }
            }

            try {
                codec.stop()
                codec.release()
                inputSurface.release()
                if (muxerStarted) {
                    muxer.stop()
                    muxer.release()
                }
            } catch (e: Exception) {
                Log.w(TAG, "Cleanup exception: ${e.message}")
            }

            onProgress(1.0f, "Render complete! HD Video ready.")
            outputFile
        } catch (e: Exception) {
            Log.e(TAG, "Video rendering error", e)
            null
        }
    }

    private fun renderCinematicFrame(
        canvas: Canvas,
        width: Int,
        height: Int,
        baseBitmap: Bitmap,
        progress: Float,
        cameraMotion: CameraMotion,
        style: StylePreset,
        paint: Paint,
        particlePaint: Paint
    ) {
        canvas.save()

        // 1. Calculate camera transform based on CameraMotion
        val scale: Float
        val transX: Float
        val transY: Float

        when (cameraMotion) {
            CameraMotion.DOLLY_PUSH -> {
                // Smooth zoom-in from 1.0f to 1.25f
                scale = 1.0f + (progress * 0.22f)
                transX = -(width * (scale - 1.0f) * 0.5f)
                transY = -(height * (scale - 1.0f) * 0.5f)
            }
            CameraMotion.PAN_HORIZONTAL -> {
                scale = 1.15f
                // Pan from left to right
                val maxShift = width * 0.12f
                transX = -maxShift + (progress * maxShift * 2) - (width * (scale - 1.0f) * 0.5f)
                transY = -(height * (scale - 1.0f) * 0.5f)
            }
            CameraMotion.DRONE_CRANE -> {
                scale = 1.18f
                // Crane ascending: shifts view downwards revealing upper vista
                val verticalShift = height * 0.15f
                transX = -(width * (scale - 1.0f) * 0.5f)
                transY = (progress * verticalShift) - (height * (scale - 1.0f) * 0.5f)
            }
            CameraMotion.ORBIT_360 -> {
                scale = 1.15f
                val orbitRadius = width * 0.04f
                val angle = progress * Math.PI.toFloat() * 2f
                transX = (cos(angle) * orbitRadius) - (width * (scale - 1.0f) * 0.5f)
                transY = (sin(angle) * orbitRadius * 0.5f) - (height * (scale - 1.0f) * 0.5f)
            }
            CameraMotion.TILT_UP -> {
                scale = 1.2f
                val tiltShift = height * 0.16f
                transX = -(width * (scale - 1.0f) * 0.5f)
                transY = -tiltShift + (progress * tiltShift) - (height * (scale - 1.0f) * 0.5f)
            }
            CameraMotion.SLOW_MO_120FPS -> {
                // Subtle breathing drift
                scale = 1.05f + (sin(progress * 3.14f) * 0.04f)
                transX = -(width * (scale - 1.0f) * 0.5f)
                transY = -(height * (scale - 1.0f) * 0.5f)
            }
            CameraMotion.DYNAMIC_HANDHELD -> {
                scale = 1.10f
                val shakeX = sin(progress * 28f) * 4f + cos(progress * 15f) * 3f
                val shakeY = cos(progress * 24f) * 3f + sin(progress * 18f) * 2f
                transX = shakeX - (width * (scale - 1.0f) * 0.5f)
                transY = shakeY - (height * (scale - 1.0f) * 0.5f)
            }
        }

        canvas.translate(transX, transY)
        canvas.scale(scale, scale)

        // Draw base bitmap
        canvas.drawBitmap(baseBitmap, 0f, 0f, paint)

        canvas.restore()

        // 2. Render atmospheric lighting & particles
        renderAtmosphericEffects(canvas, width, height, progress, style, particlePaint)

        // 3. Render cinematic vignette & anamorphic letterbox
        renderCinematicOverlays(canvas, width, height, style, particlePaint)
    }

    private fun renderAtmosphericEffects(
        canvas: Canvas,
        width: Int,
        height: Int,
        progress: Float,
        style: StylePreset,
        paint: Paint
    ) {
        val count = 28
        for (i in 0 until count) {
            val seed = i * 137.5f
            val speed = 0.3f + ((i % 5) * 0.15f)
            val currentProg = (progress * speed + (seed / 1000f)) % 1f

            val x: Float
            val y: Float
            val radius: Float

            when (style) {
                StylePreset.CYBER_NOIR -> {
                    // Rain streaks / neon particles
                    x = ((seed * 19) % width) + (currentProg * 30f)
                    y = (currentProg * (height + 200)) - 100
                    radius = 2.5f

                    paint.color = if (i % 2 == 0) Color.argb(140, 0, 229, 255) else Color.argb(130, 255, 46, 147)
                    canvas.drawLine(x, y, x - 4f, y + 16f, paint)
                }
                StylePreset.DARK_FANTASY -> {
                    // Embers rising upwards
                    x = ((seed * 23) % width) + (sin(currentProg * 10f + i) * 20f)
                    y = height - (currentProg * (height + 100))
                    radius = 3.0f + ((i % 3) * 1.5f)

                    paint.color = if (i % 3 == 0) Color.argb(180, 255, 77, 0) else Color.argb(160, 255, 183, 3)
                    canvas.drawCircle(x, y, radius, paint)
                }
                StylePreset.ANIME_SHINKAI -> {
                    // Shimmering light motes
                    x = ((seed * 31) % width) + (cos(currentProg * 6f + i) * 25f)
                    y = ((seed * 17) % height) - (currentProg * 80f)
                    radius = 2.0f + ((i % 4) * 1.2f)

                    paint.color = Color.argb(150, 255, 255, 220)
                    canvas.drawCircle(x, y, radius, paint)
                }
                else -> {
                    // Golden dust motes & optical bokeh
                    x = ((seed * 27) % width) + (sin(currentProg * 8f) * 15f)
                    y = ((seed * 19) % height) + (cos(currentProg * 5f) * 12f)
                    radius = 2.5f + ((i % 4) * 2.0f)

                    paint.color = Color.argb(120, 255, 215, 120)
                    canvas.drawCircle(x, y, radius, paint)
                }
            }
        }
    }

    private fun renderCinematicOverlays(
        canvas: Canvas,
        width: Int,
        height: Int,
        style: StylePreset,
        paint: Paint
    ) {
        // Soft cinematic vignette
        val vignetteShader = RadialGradient(
            width / 2f,
            height / 2f,
            (width.coerceAtLeast(height) * 0.72f),
            intArrayOf(Color.TRANSPARENT, Color.argb(120, 4, 7, 12)),
            floatArrayOf(0.55f, 1.0f),
            Shader.TileMode.CLAMP
        )
        paint.shader = vignetteShader
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.shader = null

        // Color grade tint
        val tintColor = when (style) {
            StylePreset.CYBER_NOIR -> Color.argb(22, 0, 160, 220)
            StylePreset.VINTAGE_35MM -> Color.argb(26, 210, 150, 60)
            StylePreset.DARK_FANTASY -> Color.argb(25, 120, 20, 40)
            StylePreset.ANIME_SHINKAI -> Color.argb(18, 0, 120, 255)
            else -> Color.argb(16, 255, 190, 40)
        }
        paint.color = tintColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun createSynthesizedBaseBitmap(width: Int, height: Int, style: StylePreset): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val topColor: Int
        val bottomColor: Int

        when (style) {
            StylePreset.CYBER_NOIR -> {
                topColor = Color.rgb(8, 12, 28)
                bottomColor = Color.rgb(18, 5, 24)
            }
            StylePreset.DARK_FANTASY -> {
                topColor = Color.rgb(20, 8, 14)
                bottomColor = Color.rgb(5, 5, 8)
            }
            StylePreset.ANIME_SHINKAI -> {
                topColor = Color.rgb(24, 78, 150)
                bottomColor = Color.rgb(240, 130, 90)
            }
            StylePreset.VINTAGE_35MM -> {
                topColor = Color.rgb(65, 45, 30)
                bottomColor = Color.rgb(25, 20, 15)
            }
            else -> {
                topColor = Color.rgb(14, 20, 35)
                bottomColor = Color.rgb(4, 6, 10)
            }
        }

        paint.shader = LinearGradient(0f, 0f, 0f, height.toFloat(), topColor, bottomColor, Shader.TileMode.CLAMP)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.shader = null

        // Add subtle geometric mountain horizon / cinematic skyline
        paint.color = Color.argb(80, 0, 0, 0)
        canvas.drawRect(0f, height * 0.65f, width.toFloat(), height.toFloat(), paint)

        return bitmap
    }
}
