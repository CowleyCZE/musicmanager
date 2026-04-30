package com.example.musicmanager.ui.screen

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.musicmanager.data.model.Song
import com.example.musicmanager.ui.component.PlayerBar
import com.example.musicmanager.viewmodel.SongViewModel

@OptIn(UnstableApi::class)
@ExperimentalMaterial3Api
@Composable
fun SongDetailScreen(
    viewModel: SongViewModel,
    navController: NavHostController,
    songId: Long
) {
    var song by remember { mutableStateOf<Song?>(null) }
    val context = LocalContext.current

    LaunchedEffect(songId) {
        song = viewModel.getSongById(songId)
    }

    val player = remember {
        ExoPlayer.Builder(context).build()
    }

    LaunchedEffect(song) {
        song?.let {
            val mediaItem = MediaItem.fromUri(Uri.parse(it.audioUri))
            player.setMediaItem(mediaItem)
            player.prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(song?.title ?: "Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = song?.coverUri,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text("Zdroj: ${song?.sourceTag}", style = MaterialTheme.typography.bodyMedium)
            Text("Popis: ${song?.musicDescription}", style = MaterialTheme.typography.bodyMedium)
            Text("Text písně:", style = MaterialTheme.typography.titleMedium)
            Text(song?.lyrics ?: "", style = MaterialTheme.typography.bodySmall)
            song?.note?.let {
                Text("Poznámka: $it", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.weight(1f))
            PlayerBar(player = player)
        }
    }
}
