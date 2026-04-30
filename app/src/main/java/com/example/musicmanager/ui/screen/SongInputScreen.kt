package com.example.musicmanager.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.musicmanager.data.model.Song
import com.example.musicmanager.viewmodel.SongViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongInputScreen(
    viewModel: SongViewModel,
    navController: NavHostController,
    songId: Long? = null
) {
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var audioUri by remember { mutableStateOf<Uri?>(null) }
    var coverUri by remember { mutableStateOf<Uri?>(null) }
    var lyrics by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var sourceTag by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    // Load existing song if editing
    LaunchedEffect(songId) {
        songId?.let {
            val song = viewModel.getSongById(it)
            song?.let { s ->
                title = s.title
                audioUri = Uri.parse(s.audioUri)
                coverUri = s.coverUri?.let { uri -> Uri.parse(uri) }
                lyrics = s.lyrics
                description = s.musicDescription
                sourceTag = s.sourceTag
                note = s.note ?: ""
            }
        }
    }

    val audioPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> audioUri = uri }

    val coverPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> coverUri = uri }

    val predefinedSources = listOf("Suno", "Producer")
    var expanded by remember { mutableStateOf(false) }
    var customSource by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (songId == null) "Nová píseň" else "Úprava písně") },
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
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Název") },
                modifier = Modifier.fillMaxWidth(),
                isError = title.isBlank()
            )
            OutlinedTextField(
                value = lyrics,
                onValueChange = { lyrics = it },
                label = { Text("Text písně") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Popis stylu") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (sourceTag.isEmpty()) "Vyberte zdroj" else sourceTag)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    predefinedSources.forEach { src ->
                        DropdownMenuItem(
                            text = { Text(src) },
                            onClick = {
                                sourceTag = src
                                expanded = false
                            }
                        )
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = customSource,
                            onValueChange = { customSource = it },
                            label = { Text("Vlastní") },
                            modifier = Modifier.width(150.dp)
                        )
                        Button(onClick = {
                            if (customSource.isNotBlank()) {
                                sourceTag = customSource
                                customSource = ""
                                expanded = false
                            }
                        }) {
                            Text("OK")
                        }
                    }
                }
            }
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Poznámka (volitelná)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedButton(onClick = { audioPicker.launch("audio/*") }, modifier = Modifier.fillMaxWidth()) {
                        Text("Audio")
                    }
                    Text(audioUri?.lastPathSegment ?: "Není", style = MaterialTheme.typography.bodySmall)
                }
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedButton(onClick = { coverPicker.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                        Text("Cover")
                    }
                    if (coverUri != null) {
                        AsyncImage(
                            model = coverUri,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (title.isNotBlank() && audioUri != null) {
                        val song = Song(
                            id = songId ?: 0L,
                            title = title,
                            audioUri = audioUri.toString(),
                            coverUri = coverUri?.toString(),
                            lyrics = lyrics,
                            musicDescription = description,
                            sourceTag = sourceTag,
                            note = note.takeIf { it.isNotBlank() }
                        )
                        scope.launch {
                            viewModel.saveSong(song)
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && audioUri != null
            ) {
                Text(if (songId == null) "Uložit" else "Aktualizovat")
            }
        }
    }
}
