package com.example.musicmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicmanager.ui.screen.PlaylistScreen
import com.example.musicmanager.ui.screen.SongDetailScreen
import com.example.musicmanager.ui.screen.SongInputScreen
import com.example.musicmanager.ui.screen.SongListScreen
import com.example.musicmanager.ui.theme.MusicManagerTheme
import com.example.musicmanager.viewmodel.SongViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicManagerTheme {
                val viewModel: SongViewModel = hiltViewModel()
                AppNavigator(viewModel)
            }
        }
    }
}

@Composable
fun AppNavigator(viewModel: SongViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "list") {
        composable("list") { SongListScreen(viewModel, navController) }
        composable("playlists") { PlaylistScreen(viewModel, navController) }
        composable("input?songId={songId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("songId")?.toLongOrNull()
            SongInputScreen(viewModel, navController, songId = id)
        }
        composable("detail/{songId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("songId")?.toLongOrNull() ?: 0L
            SongDetailScreen(viewModel, navController, songId = id)
        }
    }
}
