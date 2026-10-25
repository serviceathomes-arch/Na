package com.example.cineflow.data.repository

import com.example.R
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
import com.example.cineflow.data.model.TemplateSlot
import com.example.cineflow.data.model.VideoClip
import kotlin.random.Random

object PromptLibraryRepository {

    val promptExamples: List<PromptExample> = listOf(
        // SCI-FI
        PromptExample(
            id = "scifi_1",
            title = "Orbital Neon Metropolis",
            promptText = "A futuristic cyberpunk metropolis at midnight with glowing holographic neon billboards, flying sleek maglev skycars between towering glass spires, volumetric teal and amber fog, photorealistic 8K IMAX quality.",
            genre = GenreCategory.SCI_FI,
            style = StylePreset.CYBER_NOIR,
            complexity = ComplexityLevel.ATMOSPHERIC_VISTA,
            cameraMotion = CameraMotion.PAN_HORIZONTAL,
            lighting = "Volumetric cyan and magenta neon with damp asphalt reflections",
            languageCode = "en",
            languageName = "English",
            previewDrawableRes = R.drawable.img_hero_cinema,
            tags = listOf("Cyberpunk", "Cityscape", "Future", "8K"),
            directorNotes = "Focus on the depth layers: foreground skycars passing by while the distant megatower looms through rain haze."
        ),
        PromptExample(
            id = "scifi_2",
            title = "Saturn Ring Horizon Wanderer",
            promptText = "A solitary astronaut in an illuminated high-tech suit standing on the rocky edge of an asteroid, staring at the iridescent icy rings of Saturn as cosmic starlight refracts through their gold-tinted visor.",
            genre = GenreCategory.SCI_FI,
            style = StylePreset.CINEMATIC_8K,
            complexity = ComplexityLevel.SHORT_NARRATIVE,
            cameraMotion = CameraMotion.DOLLY_PUSH,
            lighting = "Harsh single-source solar rim light contrasting against deep cosmic black",
            languageCode = "en",
            languageName = "English",
            tags = listOf("Deep Space", "Astronaut", "Saturn", "Reflections"),
            directorNotes = "Start tight on the visor reflecting the rings, then slowly pull back revealing the vast scale of space."
        ),
        PromptExample(
            id = "scifi_3_es",
            title = "Viaje a Través del Agujero de Gusano",
            promptText = "Una nave de exploración interestelar ingresando en un agujero de gusano resplandeciente, con ondas de gravedad distorsionando la luz de las estrellas circundantes en colores violeta y oro líquido.",
            genre = GenreCategory.SCI_FI,
            style = StylePreset.ABSTRACT_SURREAL,
            complexity = ComplexityLevel.COMPLEX_CHOREOGRAPHY,
            cameraMotion = CameraMotion.ORBIT_360,
            lighting = "Chromatic gravitational lensing with shifting violet and amber flares",
            languageCode = "es",
            languageName = "Español",
            tags = listOf("Wormhole", "Spaceship", "Gravitational Lensing"),
            directorNotes = "Dynamic optical distortion simulating extreme spacetime curvature."
        ),

        // FANTASY
        PromptExample(
            id = "fantasy_1",
            title = "The Arcane Celestial Portal",
            promptText = "An ancient stone portal carved with luminous elven runes standing in a floating twilight glen, with celestial aurora ribbons weaving through the sky and golden stardust particles drifting in the breeze.",
            genre = GenreCategory.FANTASY,
            style = StylePreset.CINEMATIC_8K,
            complexity = ComplexityLevel.ATMOSPHERIC_VISTA,
            cameraMotion = CameraMotion.DRONE_CRANE,
            lighting = "Bioluminescent rune glow with twilight ambient skyfill",
            languageCode = "en",
            languageName = "English",
            previewDrawableRes = R.drawable.img_fantasy_portal,
            tags = listOf("Magic", "Portal", "Aurora", "Ancient Ruins"),
            directorNotes = "Ascend smoothly upwards to reveal the floating archipelago suspended above sea of clouds."
        ),
        PromptExample(
            id = "fantasy_2_ja",
            title = "黄昏の龍神の降臨 (Dragon God of Twilight)",
            promptText = "霧深い古の竹林の上空を優雅に舞う巨大な白龍。夕暮れの黄金色の光が龍の鱗を照らし、足元には光る蛍が舞い上がる新海誠風のアニメーション。",
            genre = GenreCategory.FANTASY,
            style = StylePreset.ANIME_SHINKAI,
            complexity = ComplexityLevel.SHORT_NARRATIVE,
            cameraMotion = CameraMotion.SLOW_MO_120FPS,
            lighting = "Golden sunset backlight with shimmering scale highlights and firefly motes",
            languageCode = "ja",
            languageName = "日本語",
            tags = listOf("Dragon", "Anime", "Twilight", "Bamboo Forest"),
            directorNotes = "Capture the fluid undulating motion of the dragon's whiskers in 120fps slow-motion."
        ),
        PromptExample(
            id = "fantasy_3",
            title = "Citadel of the Ash King",
            promptText = "A colossal gothic castle crowned with obsidian spires towering over a caldera of dormant lava, with glowing crimson cracks in the stone and embers swirling in a tempestuous ash storm.",
            genre = GenreCategory.FANTASY,
            style = StylePreset.DARK_FANTASY,
            complexity = ComplexityLevel.ATMOSPHERIC_VISTA,
            cameraMotion = CameraMotion.TILT_UP,
            lighting = "Eerie underlighting from glowing volcanic cracks and lightning flash",
            languageCode = "en",
            languageName = "English",
            tags = listOf("Gothic", "Elden Ring Style", "Volcano", "Embers"),
            directorNotes = "Emphasize monolithic scale using a dramatic low-angle perspective."
        ),

        // ROMANCE
        PromptExample(
            id = "romance_1",
            title = "Parisian Rain Cafe Reunion",
            promptText = "Two lovers meeting outside a warm glowing Parisian bistro in the gentle autumn rain, warm amber lanterns casting golden pools on wet cobblestones as steam rises from coffee cups, cinematic 35mm film.",
            genre = GenreCategory.ROMANCE,
            style = StylePreset.VINTAGE_35MM,
            complexity = ComplexityLevel.SHORT_NARRATIVE,
            cameraMotion = CameraMotion.DOLLY_PUSH,
            lighting = "Warm bistro interior amber glow contrasting against cool twilight blue rain",
            languageCode = "en",
            languageName = "English",
            tags = listOf("Paris", "Rain", "Warmth", "35mm Film"),
            directorNotes = "Soft focal blur transitioning from rain drops on glass to their joyful embrace."
        ),
        PromptExample(
            id = "romance_2_fr",
            title = "Valse des Lanternes sur la Lagune",
            promptText = "Un couple dansant une valse intemporelle sur une terrasse en marbre surplombant un lagon vénitien, tandis que des milliers de lanternes dorées s'élèvent lentement dans le ciel étoilé.",
            genre = GenreCategory.ROMANCE,
            style = StylePreset.CINEMATIC_8K,
            complexity = ComplexityLevel.SHORT_NARRATIVE,
            cameraMotion = CameraMotion.ORBIT_360,
            lighting = "Warm candlelight from thousands of floating lanterns illuminating their faces",
            languageCode = "fr",
            languageName = "Français",
            tags = listOf("Venice", "Lanterns", "Waltz", "Moonlight"),
            directorNotes = "Full 360-degree orbit gliding smoothly with the couple's dance steps."
        ),

        // ACTION / THRILLER
        PromptExample(
            id = "action_1",
            title = "Midnight Neon Highway Intercept",
            promptText = "A sleek matte-black sports car tearing down a rain-slicked highway at 160 mph, sparks flying as it weaves through heavy traffic beneath neon tunnel arches, motion blur streaks and water spray.",
            genre = GenreCategory.ACTION_THRILLER,
            style = StylePreset.CINEMATIC_8K,
            complexity = ComplexityLevel.COMPLEX_CHOREOGRAPHY,
            cameraMotion = CameraMotion.DYNAMIC_HANDHELD,
            lighting = "High-contrast headlight beams and strobing yellow tunnel lights",
            languageCode = "en",
            languageName = "English",
            tags = listOf("Speed", "Car Chase", "Rain", "High Octane"),
            directorNotes = "Ground-level chase camera vibrating with engine rumble and tire smoke."
        ),
        PromptExample(
            id = "action_2_de",
            title = "Rooftop Cyber Katana Showdown",
            promptText = "Ein Krieger im Hightech-Exo-Anzug zieht ein blitzendes Plasma-Schwert auf einem verregneten Wolkenkratzerdach in Berlin 2099, während Wassertropfen durch Blitzschlag in Zeitlupe erstarren.",
            genre = GenreCategory.ACTION_THRILLER,
            style = StylePreset.CYBER_NOIR,
            complexity = ComplexityLevel.COMPLEX_CHOREOGRAPHY,
            cameraMotion = CameraMotion.SLOW_MO_120FPS,
            lighting = "Vivid electric blue plasma blade glow illuminating falling rain droplets",
            languageCode = "de",
            languageName = "Deutsch",
            tags = listOf("Katana", "Cyberpunk", "Rain", "Lightning"),
            directorNotes = "Hyper slow-motion impact moment freezing rain droplets in mid-air."
        ),

        // NATURE & WILDLIFE
        PromptExample(
            id = "nature_1",
            title = "Fjord of the Northern Lights",
            promptText = "Sweeping IMAX aerial view of snow-dusted Norwegian fjords at night, with luminous green and violet auroras dancing across the Arctic heavens, mirrored perfectly in tranquil mirror-still waters.",
            genre = GenreCategory.NATURE_WILDLIFE,
            style = StylePreset.AERIAL_IMAX,
            complexity = ComplexityLevel.ATMOSPHERIC_VISTA,
            cameraMotion = CameraMotion.DRONE_CRANE,
            lighting = "Ethereal emerald and violet polar auroras providing ambient luminescence",
            languageCode = "en",
            languageName = "English",
            tags = listOf("Norway", "Aurora", "Fjords", "IMAX"),
            directorNotes = "Epic slow forward flight maintaining glassy water reflections."
        ),
        PromptExample(
            id = "nature_2",
            title = "Bioluminescent Abyssal Coral Reef",
            promptText = "Deep ocean expedition camera gliding through an undiscovered underwater canyon filled with glowing cyan coral trees, pulsating translucent jellyfish, and micro-sparkles drifting in the current.",
            genre = GenreCategory.NATURE_WILDLIFE,
            style = StylePreset.STUDIO_3D_CGI,
            complexity = ComplexityLevel.SIMPLE_SCENE,
            cameraMotion = CameraMotion.DOLLY_PUSH,
            lighting = "Organic cyan and magenta bioluminescence piercing through midnight ocean depth",
            languageCode = "en",
            languageName = "English",
            tags = listOf("Underwater", "Bioluminescence", "Jellyfish", "Deep Ocean"),
            directorNotes = "Dreamy underwater physics with gentle floating particulate motes."
        ),

        // HISTORICAL
        PromptExample(
            id = "hist_1",
            title = "1920s Harlem Jazz Speakeasy",
            promptText = "A smoky, vibrant 1920s jazz club in New York, a brass trumpet soloist performing with passion under a warm spotlight, couples dancing in silhouette, cigarette smoke curling through amber rays, 35mm film grain.",
            genre = GenreCategory.HISTORICAL,
            style = StylePreset.VINTAGE_35MM,
            complexity = ComplexityLevel.SHORT_NARRATIVE,
            cameraMotion = CameraMotion.DOLLY_PUSH,
            lighting = "Single warm amber overhead spotlight carving through hazy smoke",
            languageCode = "en",
            languageName = "English",
            tags = listOf("1920s", "Jazz", "Trumpet", "Vintage"),
            directorNotes = "Authentic Kodachrome warm color grade with delicate film flutter."
        ),

        // HORROR & MYSTERY
        PromptExample(
            id = "horror_1",
            title = "Midnight Fog at the Lighthouse",
            promptText = "A lonely Victorian lighthouse perched on jagged black coastal rocks during a violent midnight squall, its revolving golden beam slicing through thick rolling sea mist as waves shatter against granite.",
            genre = GenreCategory.HORROR_MYSTERY,
            style = StylePreset.CINEMATIC_8K,
            complexity = ComplexityLevel.ATMOSPHERIC_VISTA,
            cameraMotion = CameraMotion.PAN_HORIZONTAL,
            lighting = "Revolving piercing beacon ray piercing through dark foggy mist",
            languageCode = "en",
            languageName = "English",
            tags = listOf("Lighthouse", "Storm", "Mystery", "Ocean"),
            directorNotes = "Capture the moment the revolving beam sweeps directly into camera creating lens flare."
        ),

        // SIMPLE SCENE COMPLEXITY
        PromptExample(
            id = "simple_1",
            title = "Golden Dewdrop Bloom",
            promptText = "Macro cinematic shot of a single crystal dewdrop perched on a velvet red rose petal at dawn, reflecting the rising sun in microscopic detail as morning mist gently curls.",
            genre = GenreCategory.NATURE_WILDLIFE,
            style = StylePreset.CINEMATIC_8K,
            complexity = ComplexityLevel.SIMPLE_SCENE,
            cameraMotion = CameraMotion.DOLLY_PUSH,
            lighting = "Warm early dawn sunbeam catching internal caustics of the water droplet",
            languageCode = "en",
            languageName = "English",
            tags = listOf("Macro", "Rose", "Dewdrop", "Dawn"),
            directorNotes = "Extreme shallow depth of field (f/1.2) creating creamy background bokeh."
        )
    )

    val promptTemplates: List<PromptTemplate> = listOf(
        PromptTemplate(
            id = "template_epic_reveal",
            title = "Epic Cinematic Reveal",
            description = "Ascending or pushing camera shot revealing a colossal subject in a dramatic landscape.",
            genre = GenreCategory.SCI_FI,
            templatePattern = "A cinematic {camera_motion} revealing {subject} situated in {setting}, illuminated by {lighting}, rendered in {style_preset} with {atmosphere}.",
            slots = listOf(
                TemplateSlot(
                    key = "camera_motion",
                    label = "Camera Motion",
                    defaultVal = "smooth low-angle crane ascending upwards",
                    suggestedOptions = listOf(
                        "smooth low-angle crane ascending upwards",
                        "slow cinematic dolly push-in",
                        "sweeping 360-degree orbital rotation",
                        "dramatic aerial tracking shot"
                    )
                ),
                TemplateSlot(
                    key = "subject",
                    label = "Central Subject",
                    defaultVal = "a colossal ancient cybernetic monolith",
                    suggestedOptions = listOf(
                        "a colossal ancient cybernetic monolith",
                        "an interstellar dreadnought docking at a space ring",
                        "a solitary samurai standing beneath cherry blossoms",
                        "a crystalline dragon awakening from slumber",
                        "an abandoned steam locomotive overgrown with ivy"
                    )
                ),
                TemplateSlot(
                    key = "setting",
                    label = "Environment / Setting",
                    defaultVal = "a mist-covered obsidian canyon",
                    suggestedOptions = listOf(
                        "a mist-covered obsidian canyon",
                        "a rain-slicked Tokyo neon skyscraper roof",
                        "a sun-drenched desert with glowing sand dunes",
                        "a floating archipelago above golden clouds",
                        "an ancient submerged Atlantis palace"
                    )
                ),
                TemplateSlot(
                    key = "lighting",
                    label = "Lighting Scheme",
                    defaultVal = "volumetric god rays piercing through dense fog",
                    suggestedOptions = listOf(
                        "volumetric god rays piercing through dense fog",
                        "dramatic cyan and magenta neon rim lighting",
                        "warm golden hour sunset backlighting",
                        "eerie crimson volcanic underglow",
                        "celestial green aurora borealis luminescence"
                    )
                ),
                TemplateSlot(
                    key = "style_preset",
                    label = "Art & Lens Style",
                    defaultVal = "35mm Arri Alexa 8K cinematic quality",
                    suggestedOptions = listOf(
                        "35mm Arri Alexa 8K cinematic quality",
                        "Makoto Shinkai anime watercolor aesthetic",
                        "Blade Runner cyberpunk neo-noir 4K",
                        "1970s Technicolor vintage film with warm grain",
                        "Pixar 3D CGI stylized animation with soft lighting"
                    )
                ),
                TemplateSlot(
                    key = "atmosphere",
                    label = "Atmospheric FX",
                    defaultVal = "floating embers and soft anamorphic lens flare",
                    suggestedOptions = listOf(
                        "floating embers and soft anamorphic lens flare",
                        "microscopic rain droplets catching light streaks",
                        "swirling golden pollen dust motes",
                        "dense rolling ocean mist and sea spray"
                    )
                )
            )
        ),
        PromptTemplate(
            id = "template_romantic_moment",
            title = "Poetic Emotional Moment",
            description = "Intimate cinematic portraiture capturing mood, warmth, and human emotion.",
            genre = GenreCategory.ROMANCE,
            templatePattern = "An emotional, intimate cinematic scene of {subject} {activity} in {setting}, with {lighting}, captured in {style_preset} with {atmosphere}.",
            slots = listOf(
                TemplateSlot(
                    key = "subject",
                    label = "Characters",
                    defaultVal = "two lovers holding hands",
                    suggestedOptions = listOf(
                        "two lovers holding hands",
                        "a lone painter finishing a portrait",
                        "a musician playing cello on a quiet bridge",
                        "two friends laughing together under an umbrella"
                    )
                ),
                TemplateSlot(
                    key = "activity",
                    label = "Action / Expression",
                    defaultVal = "sharing a quiet smile of profound recognition",
                    suggestedOptions = listOf(
                        "sharing a quiet smile of profound recognition",
                        "dancing slowly as the music fades into twilight",
                        "staring at the vast ocean horizon in quiet peace",
                        "whispering secrets as lanterns drift overhead"
                    )
                ),
                TemplateSlot(
                    key = "setting",
                    label = "Location",
                    defaultVal = "a vintage cobblestone street in Florence",
                    suggestedOptions = listOf(
                        "a vintage cobblestone street in Florence",
                        "a secluded misty beach at twilight",
                        "a cozy candlelit greenhouse during a thunderstorm",
                        "a rooftop overlooking a sparkling city skyline"
                    )
                ),
                TemplateSlot(
                    key = "lighting",
                    label = "Lighting Mood",
                    defaultVal = "golden sunset rays and soft candle glow",
                    suggestedOptions = listOf(
                        "golden sunset rays and soft candle glow",
                        "warm streetlamp amber reflections on wet pavement",
                        "diffused morning window light with soft shadows",
                        "cool moonlight contrasting against warm fireside"
                    )
                ),
                TemplateSlot(
                    key = "style_preset",
                    label = "Style",
                    defaultVal = "35mm vintage film with creamy bokeh",
                    suggestedOptions = listOf(
                        "35mm vintage film with creamy bokeh",
                        "8K photoreal cinema with 50mm f/1.2 lens",
                        "Studio Ghibli painterly watercolor aesthetic",
                        "French New Wave black and white with high contrast"
                    )
                ),
                TemplateSlot(
                    key = "atmosphere",
                    label = "Atmosphere",
                    defaultVal = "soft autumn leaves falling gently in slow motion",
                    suggestedOptions = listOf(
                        "soft autumn leaves falling gently in slow motion",
                        "gentle rain dripping from café awnings",
                        "drifting golden fireflies in the twilight air"
                    )
                )
            )
        ),
        PromptTemplate(
            id = "template_action_rush",
            title = "High-Octane Action Dynamic",
            description = "Fast-paced, kinetic sequence with rapid momentum and dynamic camera choreography.",
            genre = GenreCategory.ACTION_THRILLER,
            templatePattern = "A high-octane action sequence showing {subject} {action_verb} through {setting}, filmed with {camera_motion}, featuring {lighting} and {atmosphere}, {style_preset}.",
            slots = listOf(
                TemplateSlot(
                    key = "subject",
                    label = "Protagonist / Vehicle",
                    defaultVal = "a rogue cybernetic courier on a neon-lit hoverbike",
                    suggestedOptions = listOf(
                        "a rogue cybernetic courier on a neon-lit hoverbike",
                        "a master martial artist in a flowing silk trench coat",
                        "a stealth spy escaping a collapsing glass laboratory",
                        "a fighter jet breaking the sound barrier through clouds"
                    )
                ),
                TemplateSlot(
                    key = "action_verb",
                    label = "Dynamic Action",
                    defaultVal = "evading pursuers with extreme high-speed maneuvers",
                    suggestedOptions = listOf(
                        "evading pursuers with extreme high-speed maneuvers",
                        "deflecting kinetic projectiles with a plasma shield",
                        "vaulting across vertigo-inducing rooftop cranes",
                        "drifting through narrow neon alleyways at 140 mph"
                    )
                ),
                TemplateSlot(
                    key = "setting",
                    label = "Arena / Obstacle",
                    defaultVal = "a dense multi-level futuristic megacity",
                    suggestedOptions = listOf(
                        "a dense multi-level futuristic megacity",
                        "an exploding offshore oil rig in a stormy ocean",
                        "a speeding freight train winding through snow mountains",
                        "a subterranean particle accelerator facility"
                    )
                ),
                TemplateSlot(
                    key = "camera_motion",
                    label = "Camera Velocity",
                    defaultVal = "intense low-slung chase cam with motion blur",
                    suggestedOptions = listOf(
                        "intense low-slung chase cam with motion blur",
                        "120fps bullet-time slow motion rotating 360 degrees",
                        "dynamic handheld camera tracking closely behind",
                        "fast whip-pan snapping between hero and obstacle"
                    )
                ),
                TemplateSlot(
                    key = "lighting",
                    label = "Strobing & FX",
                    defaultVal = "strobe security alarms and vibrant neon trails",
                    suggestedOptions = listOf(
                        "strobe security alarms and vibrant neon trails",
                        "blinding lightning strikes flashing in darkness",
                        "sparkling exhaust flame backfire illumination",
                        "high-contrast searchlight beams cutting through rain"
                    )
                ),
                TemplateSlot(
                    key = "atmosphere",
                    label = "Physical Dynamics",
                    defaultVal = "flying glass shards and water spray in slow motion",
                    suggestedOptions = listOf(
                        "flying glass shards and water spray in slow motion",
                        "drifting tire smoke and ozone sparks",
                        "crashing waves breaking over metal walkways"
                    )
                ),
                TemplateSlot(
                    key = "style_preset",
                    label = "Film Profile",
                    defaultVal = "IMAX action film with Christopher Nolan realism",
                    suggestedOptions = listOf(
                        "IMAX action film with Christopher Nolan realism",
                        "hyper-stylized anime sakuga with ink strokes",
                        "gritty 90s thriller 35mm grain",
                        "glossy high-definition sci-fi blockbuster"
                    )
                )
            )
        )
    )

    // RANDOM PROMPT GENERATOR VOCABULARY ENGINE
    private val randomSubjects = listOf(
        "A lone cybernetic samurai with glowing indigo circuits",
        "A majestic bioluminescent whale gliding through a cloudscape",
        "A celestial architect weaving galaxies from liquid gold",
        "An armored space explorer discovering a crystalline alien flora",
        "A graceful dancer in a billowing silk gown under rain",
        "A steam-powered locomotive charging across a suspended viaduct",
        "A mythical phoenix with feather tips of prismatic flame",
        "A deep-sea robotic rover investigating an abyssal ruin",
        "A curious red fox observing an aurora borealis on a snowy peak",
        "A cloaked alchemist brewing glowing violet starlight in an observatory",
        "A neon-trimmed racing hovercraft drifting on a flooded freeway",
        "An ancient stone colossus slowly turning its head towards the sun"
    )

    private val randomSettings = listOf(
        "inside a rainy neo-Tokyo metropolis lit by towering holographic signs",
        "perched atop a misty Himalayan summit as sunrise breaches clouds",
        "on the rim of an alien asteroid overlooking Saturn's icy rings",
        "within an overgrown Victorian botanical garden during a thunderstorm",
        "at the edge of an ethereal turquoise lagoon with floating lanterns",
        "in a forgotten subterranean library carved into subterranean crystal",
        "inside a retro-futuristic jazz lounge drenched in warm amber haze",
        "gliding over pristine Nordic fjords reflecting vivid polar lights",
        "in a scorched volcanic caldera crisscrossed with glowing lava veins",
        "in an infinite desert where the sand dunes shimmer like crushed diamonds"
    )

    private val randomLighting = listOf(
        "volumetric god rays piercing through rising mist and dust motes",
        "dramatic chiaroscuro lighting with sharp teal and magenta neon rim lights",
        "warm golden hour sunset creating rich amber glows and long silhouettes",
        "eerie bioluminescent cyan glow piercing deep darkness",
        "single piercing overhead spotlight creating intense atmospheric shadows",
        "soft pastel twilight diffusion with iridescent chromatic aberration"
    )

    private val randomCameraActions = listOf(
        "smooth cinematic dolly-in with shallow depth of field",
        "sweeping drone crane shot ascending gracefully to reveal the horizon",
        "dynamic 360-degree orbital rotation around the central focal subject",
        "slow horizontal tracking pan across the breathtaking panoramic expanse",
        "dramatic low-angle tilt upwards emphasizing monolithic monumental scale",
        "ultra-slow motion 120fps capture freezing floating particles in mid-air"
    )

    private val randomAtmospheres = listOf(
        "subtle anamorphic lens flare with microscopic dust motes floating in air",
        "gentle rain droplets reflecting neon lights as steam rises from pavement",
        "glowing embers and ash particles swirling in the gentle thermal draft",
        "celestial starfield refractions shimmering across polished glass",
        "wispy tendrils of morning fog creeping between natural rock formations"
    )

    fun generateRandomPrompt(
        preferredGenre: GenreCategory = GenreCategory.ALL,
        preferredStyle: StylePreset? = null
    ): GeneratedPromptResult {
        val subject = randomSubjects.random()
        val setting = randomSettings.random()
        val lighting = randomLighting.random()
        val camera = randomCameraActions.random()
        val atmosphere = randomAtmospheres.random()

        val style = preferredStyle ?: StylePreset.entries.random()
        val genre = if (preferredGenre == GenreCategory.ALL) {
            listOf(GenreCategory.SCI_FI, GenreCategory.FANTASY, GenreCategory.ROMANCE, GenreCategory.ACTION_THRILLER, GenreCategory.NATURE_WILDLIFE, GenreCategory.CYBERPUNK).random()
        } else {
            preferredGenre
        }

        val cameraMotion = CameraMotion.entries.random()
        val complexity = ComplexityLevel.entries.random()

        val fullPrompt = "$subject $setting. $lighting. Captured with $camera, featuring $atmosphere. ${style.promptModifier}."

        val title = generateCreativeTitle(subject, style)

        return GeneratedPromptResult(
            title = title,
            promptText = fullPrompt,
            genre = genre,
            style = style,
            cameraMotion = cameraMotion,
            complexity = complexity,
            lighting = lighting,
            directorNotes = "Engineered for optimal camera path fluidity, dramatic optical lighting contrast, and high-definition particle dynamics."
        )
    }

    private fun generateCreativeTitle(subject: String, style: StylePreset): String {
        val cleanSubject = subject.removePrefix("A ").removePrefix("An ")
            .split(" ")
            .take(3)
            .joinToString(" ")
        return "${cleanSubject.replaceFirstChar { it.uppercase() }} — ${style.displayName}"
    }

    val initialClips: List<VideoClip> = listOf(
        VideoClip(
            id = "demo_clip_1",
            title = "Orbital Neon Metropolis",
            prompt = "A futuristic cyberpunk metropolis at midnight with glowing holographic neon billboards, flying sleek maglev skycars, volumetric teal and amber fog, photorealistic 8K IMAX quality.",
            enhancedPrompt = "A breathtaking 8K IMAX wide tracking shot of a cyberpunk metropolis in the year 2142. Sleek maglev skycars drift along illuminated light-corridors between crystalline mega-spires. Volumetric cyan and amber fog rolls through the streets, reflecting in damp asphalt. Anamorphic horizontal lens flare, Arri Alexa 8K camera, 60fps.",
            genre = GenreCategory.SCI_FI,
            style = StylePreset.CYBER_NOIR,
            cameraMotion = CameraMotion.PAN_HORIZONTAL,
            aspectRatio = AspectRatioOption.WIDESCREEN_16_9,
            resolution = ResolutionOption.HD_1080P,
            audioSoundscape = AudioSoundscape.CYBER_PULSE,
            durationSeconds = 6,
            previewDrawableRes = R.drawable.img_hero_cinema,
            isFavorite = true,
            directorBreakdown = DirectorBreakdown(
                lensOptics = "35mm Anamorphic T/1.5 Cine Lens",
                cameraRig = "Technocrane on 30m track with gyro stabilization",
                colorGrading = "Cyber Noir LUT: Cyan shadows, high-key magenta & amber highlights",
                lightingSetup = "Volumetric neon street spill, backlit skycar trails, wet asphalt bounce",
                particleAtmosphere = "Fine acid rain mist & holographic raster scanlines",
                audioCue = "Deep pulsing synth bassline with metallic city hum"
            )
        ),
        VideoClip(
            id = "demo_clip_2",
            title = "The Arcane Celestial Portal",
            prompt = "An ancient stone portal carved with luminous elven runes standing in a floating twilight glen, with celestial aurora ribbons weaving through the sky and golden stardust particles drifting in the breeze.",
            enhancedPrompt = "An ethereal slow crane shot ascending past an ancient basalt ring engraved with pulsating gold-leaf runes. Beyond the threshold, a celestial rift reveals violet nebula clouds and glowing islands suspended in zero gravity. Shimmering firefly particles, 35mm film grain, 24fps cinema.",
            genre = GenreCategory.FANTASY,
            style = StylePreset.CINEMATIC_8K,
            cameraMotion = CameraMotion.DRONE_CRANE,
            aspectRatio = AspectRatioOption.ANAMORPHIC_2_39,
            resolution = ResolutionOption.UHD_4K,
            audioSoundscape = AudioSoundscape.CINEMATIC_ORCHESTRAL,
            durationSeconds = 8,
            previewDrawableRes = R.drawable.img_fantasy_portal,
            isFavorite = false,
            directorBreakdown = DirectorBreakdown(
                lensOptics = "50mm Prime f/1.2 Master Cine Prime",
                cameraRig = "Heavy-lift cinematic drone with optical flow stabilization",
                colorGrading = "Ethereal Fantasy: Deep indigo shadows with glowing gold highlights",
                lightingSetup = "Bioluminescent rune emission with twin moon twilight backlight",
                particleAtmosphere = "Floating celestial mana motes with light caustics",
                audioCue = "Choral ambient choir swelling into French horn motif"
            )
        )
    )
}

data class GeneratedPromptResult(
    val title: String,
    val promptText: String,
    val genre: GenreCategory,
    val style: StylePreset,
    val cameraMotion: CameraMotion,
    val complexity: ComplexityLevel,
    val lighting: String,
    val directorNotes: String
)
