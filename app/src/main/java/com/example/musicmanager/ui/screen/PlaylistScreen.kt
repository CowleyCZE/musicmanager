package com.example.musicmanager.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.musicmanager.data.model.Playlist
import com.example.musicmanager.viewmodel.SongViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    viewModel: SongViewModel,
    navController: NavHostController
) {
    val playlists by viewModel.playlists.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var playlistName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Playlisty") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Nový playlist")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(playlists) { playlist ->
                PlaylistRow(playlist = playlist, onClick = {
                    // Navigate to playlist detail (optional expansion)
                })
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Nový playlist") },
                text = {
                    OutlinedTextField(
                        value = playlistName,
                        onValueChange = { playlistName = it },
                        label = { Text("Název") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (playlistName.isNotBlank()) {
                            viewModel.createPlaylist(playlistName)
                            playlistName = ""
                            showDialog = false
                        }
                    }) { Text("Vytvořit") }
                }
            )
        }
    }
}

@Composable
fun PlaylistRow(playlist: Playlist, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(playlist.name, style = MaterialTheme.typography.titleLarge)
        }
    }
}
