package com.example.musicmanager.data.repository

import com.example.musicmanager.data.dao.SongDao
import com.example.musicmanager.data.model.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongRepository @Inject constructor(
    private val songDao: SongDao
) {
    fun getAllSongs(): Flow<List<Song>> = songDao.getAllSongs()
    fun searchSongs(query: String): Flow<List<Song>> = songDao.searchSongs("%$query%")

    suspend fun insert(song: Song) = songDao.insert(song)
    suspend fun update(song: Song) = songDao.update(song)
    suspend fun delete(song: Song) = songDao.delete(song)
    suspend fun getById(id: Long) = songDao.getById(id)
}
