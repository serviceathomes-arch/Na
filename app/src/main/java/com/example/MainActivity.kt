package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cineflow.data.local.CineDatabase
import com.example.cineflow.data.repository.VideoRepository
import com.example.cineflow.ui.AppScreen
import com.example.cineflow.ui.CineFlowViewModel
import com.example.cineflow.ui.components.CineFlowBottomBar
import com.example.cineflow.ui.screens.PlayerScreen
import com.example.cineflow.ui.screens.ProjectsScreen
import com.example.cineflow.ui.screens.PromptLibraryScreen
import com.example.cineflow.ui.screens.RandomPromptScreen
import com.example.cineflow.ui.screens.StudioScreen
import androidx.compose.ui.platform.LocalContext
import com.example.cineflow.ui.components.SocialPublishDialog
import com.example.ui.theme.CineFlowTheme
import com.example.ui.theme.CineObsidian

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = CineDatabase.getInstance(applicationContext)
        val repository = VideoRepository(database)

        val viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CineFlowViewModel(repository) as T
            }
        }

        setContent {
            CineFlowTheme {
                val vm: CineFlowViewModel = viewModel(factory = viewModelFactory)
                CineFlowApp(vm)
            }
        }
    }
}

@Composable
fun CineFlowApp(viewModel: CineFlowViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

    // Handle back button when navigating sub-screens
    if (currentScreen != AppScreen.STUDIO) {
        BackHandler {
            viewModel.navigateTo(AppScreen.STUDIO)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CineObsidian),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            CineFlowBottomBar(
                currentScreen = currentScreen,
                onNavigate = { viewModel.navigateTo(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CineObsidian)
        ) {
            when (currentScreen) {
                AppScreen.STUDIO -> StudioScreen(viewModel = viewModel)
                AppScreen.LIBRARY -> PromptLibraryScreen(viewModel = viewModel)
                AppScreen.RANDOMIZER -> RandomPromptScreen(viewModel = viewModel)
                AppScreen.PLAYER -> PlayerScreen(viewModel = viewModel)
                AppScreen.PROJECTS -> ProjectsScreen(viewModel = viewModel)
            }
        }
    }

    val socialPublishState by viewModel.socialPublishState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    SocialPublishDialog(
        state = socialPublishState,
        onSelectPlatform = { viewModel.selectPublishPlatform(it) },
        onUpdateCaption = { viewModel.updateSocialCaption(it) },
        onUpload = { platform, caption ->
            val targetClip = socialPublishState.targetClip
            if (targetClip != null) {
                viewModel.uploadToSocialMedia(context, targetClip, platform, caption)
            }
        },
        onDismiss = { viewModel.closeSocialPublishDialog() }
    )
}
