package com.example.musicmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicmanager.data.model.Song
import com.example.musicmanager.data.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.musicmanager.data.dao.PlaylistDao
import com.example.musicmanager.data.model.Playlist
import com.example.musicmanager.data.model.PlaylistSongCrossRef
import com.example.musicmanager.util.JsonExporter
import android.content.Context
import java.io.OutputStream

@HiltViewModel
class SongViewModel @Inject constructor(
    private val repository: SongRepository,
    private val playlistDao: PlaylistDao
) : ViewModel() {

    val songs = repository.getAllSongs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val playlists = playlistDao.getAllPlaylists()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun search(query: String) = viewModelScope.launch {
        // Logika pro vyhledávání (v této verzi repository vrací flow s filtrem)
    }

    fun saveSong(song: Song) = viewModelScope.launch {
        if (song.id == 0L) repository.insert(song) else repository.update(song)
    }

    fun deleteSong(song: Song) = viewModelScope.launch {
        repository.delete(song)
    }

    suspend fun getSongById(id: Long) = repository.getById(id)

    // Playlist logic
    fun createPlaylist(name: String) = viewModelScope.launch {
        playlistDao.insertPlaylist(Playlist(name = name))
    }

    fun addSongToPlaylist(songId: Long, playlistId: Long) = viewModelScope.launch {
        playlistDao.addSongToPlaylist(PlaylistSongCrossRef(playlistId, songId))
    }

    fun getSongsInPlaylist(playlistId: Long) = playlistDao.getSongsInPlaylist(playlistId)

    // Export logic
    fun exportData(context: Context, outputStream: OutputStream) {
        viewModelScope.launch {
            val allSongs = songs.value
            JsonExporter.exportSongsToJson(context, allSongs, outputStream)
        }
    }
}
