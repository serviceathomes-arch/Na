package com.example.cineflow.data.model

enum class SocialPlatform(
    val displayName: String,
    val packageName: String?,
    val recommendedRatio: AspectRatioOption,
    val hashtags: String,
    val colorHex: Long
) {
    ALL("All Social Media", null, AspectRatioOption.VERTICAL_9_16, "#AIvideo #TextToVideo #Cinematic #CineFlow #Viral", 0xFFFFB703),
    YOUTUBE_SHORTS("YouTube Shorts", "com.google.android.youtube", AspectRatioOption.VERTICAL_9_16, "#Shorts #AIvideo #Cinematic #CineFlow #YouTubeShorts", 0xFFFF0000),
    TIKTOK("TikTok", "com.zhiliaoapp.musically", AspectRatioOption.VERTICAL_9_16, "#fyp #cinematic #aivideo #text2video #viralvideo #trending", 0xFF00F2FE),
    INSTAGRAM_REELS("Instagram Reels", "com.instagram.android", AspectRatioOption.VERTICAL_9_16, "#reels #reelsinstagram #aivideo #cinematography #cineflow #explorepage", 0xFFE1306C),
    X_TWITTER("X (Twitter)", "com.twitter.android", AspectRatioOption.WIDESCREEN_16_9, "#AI #VideoAI #Cinematic #AIArt #CineFlow #Tech", 0xFF1DA1F2),
    WHATSAPP("WhatsApp", "com.whatsapp", AspectRatioOption.VERTICAL_9_16, "#CineFlow #CinematicVideo", 0xFF25D366)
}
