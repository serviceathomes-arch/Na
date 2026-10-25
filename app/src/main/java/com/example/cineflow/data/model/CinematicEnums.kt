package com.example.cineflow.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.vector.ImageVector

enum class GenreCategory(
    val displayName: String,
    val icon: ImageVector,
    val description: String
) {
    ALL("All Genres", Icons.Default.AutoAwesome, "Explore prompts across every cinematic universe"),
    SCI_FI("Sci-Fi", Icons.Default.RocketLaunch, "Cybernetics, deep space, warp jumps, alien horizons"),
    FANTASY("Fantasy", Icons.Default.AutoAwesome, "Mythic beasts, ancient runes, glowing magic citadels"),
    ROMANCE("Romance", Icons.Default.Favorite, "Poetic encounters, golden hour warmth, emotional depth"),
    ACTION_THRILLER("Action", Icons.Default.Bolt, "High-octane pursuits, dynamic stunts, intense tension"),
    NATURE_WILDLIFE("Nature", Icons.Default.Landscape, "Majestic biomes, wildlife close-ups, weather extremes"),
    HISTORICAL("Historical", Icons.Default.HistoryEdu, "Period eras, classic architecture, vintage aesthetics"),
    HORROR_MYSTERY("Mystery & Horror", Icons.Default.Visibility, "Atmospheric dread, gothic shadows, eerie secrets"),
    CYBERPUNK("Cyberpunk", Icons.Default.Nightlife, "Rain-slicked neon alleys, holograms, synth aesthetics")
}

enum class StylePreset(
    val displayName: String,
    val tag: String,
    val promptModifier: String,
    val colorTone: String
) {
    CINEMATIC_8K(
        "Cinematic 8K",
        "Photoreal",
        "shot on 35mm Arri Alexa 8K, anamorphic lens flare, Kodak Portra film grain, depth of field, masterpiece",
        "Warm Gold / Obsidian"
    ),
    ANIME_SHINKAI(
        "Anime Shinkai",
        "Anime",
        "vibrant Makoto Shinkai aesthetic, painterly twilight sky, glowing particles, detailed anime background, Studio CoMix Wave style",
        "Sky Azure / Sunset Coral"
    ),
    CYBER_NOIR(
        "Cyber Noir",
        "Cyberpunk",
        "Blade Runner neo-noir style, volumetric cyan and magenta neon lighting, wet asphalt reflections, steam vents, 4K filmic",
        "Cyan / Neon Pink"
    ),
    STUDIO_3D_CGI(
        "Studio 3D CGI",
        "3D Stylized",
        "Pixar and Disney 3D animation style, subsurface scattering, expressive lighting, soft volumetric shadows, ultra clean rendering",
        "Vibrant Amber / Lilac"
    ),
    DARK_FANTASY(
        "Dark Fantasy",
        "Gothic",
        "Elden Ring dark fantasy aesthetic, dramatic chiaroscuro lighting, floating embers, ancient spires, melancholic majesty",
        "Crimson / Ash Grey"
    ),
    VINTAGE_35MM(
        "Vintage 35mm",
        "Retro 1970s",
        "1970s Technicolor vintage cinema, soft lens diffusion, authentic film flutter, nostalgic warm color grading, Super 8 grain",
        "Sepia / Pastel Teal"
    ),
    ABSTRACT_SURREAL(
        "Surrealism",
        "Surreal",
        "surreal liquid chrome dreamscape, kaleidoscopic floating geometry, Dali and Moebius inspiration, impossible physics, transcendent lighting",
        "Iridescent / Violet"
    ),
    AERIAL_IMAX(
        "Aerial IMAX",
        "Documentary",
        "sweeping IMAX documentary aerial footage, ultra wide-angle lens, hyper-crisp geological detail, majestic natural scale",
        "Emerald / Deep Blue"
    )
}

enum class ComplexityLevel(
    val displayName: String,
    val levelNumber: Int,
    val description: String
) {
    SIMPLE_SCENE("Simple Scene", 1, "Single focal subject with smooth ambient atmosphere"),
    ATMOSPHERIC_VISTA("Atmospheric Vista", 2, "Sweeping environmental scenery with lighting interplay"),
    SHORT_NARRATIVE("Short Narrative", 3, "Multi-beat cinematic sequence with character action"),
    COMPLEX_CHOREOGRAPHY("Complex Choreography", 4, "Dynamic multi-subject interaction, rapid motion, and camera shifts")
}

enum class CameraMotion(
    val displayName: String,
    val promptDescription: String
) {
    DOLLY_PUSH("Dolly Push-In", "smooth slow dolly push-in camera movement towards focal point"),
    PAN_HORIZONTAL("Sweeping Pan", "cinematic horizontal tracking pan across the panoramic scene"),
    DRONE_CRANE("Drone Crane Shot", "sweeping vertical crane shot ascending upwards revealing the grand environment"),
    ORBIT_360("360° Orbit", "dynamic orbital 360-degree camera rotation around the central subject"),
    TILT_UP("Low Angle Tilt", "dramatic low-angle camera tilting smoothly upwards towards the sky"),
    SLOW_MO_120FPS("120fps Slow-Mo", "ultra-slow motion 120fps capture with time-dilation effect on particles"),
    DYNAMIC_HANDHELD("Cinematic Handheld", "immersive subtle handheld camera motion with organic micro-shakes")
}

enum class AspectRatioOption(
    val label: String,
    val ratio: Float,
    val description: String
) {
    WIDESCREEN_16_9("16:9", 16f / 9f, "Cinematic Landscape / YouTube"),
    VERTICAL_9_16("9:16", 9f / 16f, "Vertical Reel / Shorts / TikTok"),
    ANAMORPHIC_2_39("2.39:1", 2.39f, "Anamorphic Cinemascope Ultrawide"),
    SQUARE_1_1("1:1", 1f, "Square Social Feed"),
    CLASSIC_4_3("4:3", 4f / 3f, "Classic Academy Vintage")
}

enum class ResolutionOption(
    val label: String,
    val width: Int,
    val height: Int,
    val fps: Int,
    val badge: String
) {
    HD_1080P("1080p FHD", 1920, 1080, 60, "HD 60FPS"),
    UHD_4K("4K Ultra HD", 3840, 2160, 24, "4K CINEMA"),
    FAST_720P("720p Fast", 1280, 720, 30, "FAST RENDER")
}

enum class AudioSoundscape(
    val displayName: String,
    val genreTheme: String,
    val description: String
) {
    CINEMATIC_ORCHESTRAL("Cinematic Orchestral", "Epic Strings", "Rich cello swells, subtle French horns, and atmospheric percussion"),
    CYBER_PULSE("Cyber Synth Pulse", "Electronic", "Deep analog synth bassline with shimmering neon arpeggios"),
    RAIN_AND_THUNDER("Rain & Thunder", "Atmospheric", "Gentle rain dripping against glass with distant rumbling thunder"),
    DEEP_SPACE_DRONE("Deep Space Drone", "Cosmic Ambient", "Hypnotic low-frequency drone with crystalline celestial chimes"),
    NATURE_WILDLIFE("Nature & Wind", "Organic", "Soft forest breezes, rustling leaves, and subtle dawn birdsong"),
    MUTED("Silent", "No Audio", "Render clip without soundtrack")
}

enum class DurationOption(
    val label: String,
    val seconds: Int,
    val description: String,
    val isEpic: Boolean = false
) {
    SEC_5("5s", 5, "Quick Story"),
    SEC_15("15s", 15, "Social Clip"),
    SEC_30("30s", 30, "Teaser Trailer"),
    MIN_1("1 Min", 60, "Short Narrative"),
    MIN_5("5 Min", 300, "Extended Feature"),
    MIN_10("10 Min", 600, "10 Minutes Epic Cinema", true)
}

