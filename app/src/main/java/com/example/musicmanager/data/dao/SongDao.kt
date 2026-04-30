package com.example.musicmanager.data.dao

import androidx.room.*
import com.example.musicmanager.data.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query("SELECT * FROM songs ORDER BY timestamp DESC")
    fun getAllSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE title LIKE :search ORDER BY timestamp DESC")
    fun searchSongs(search: String): Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: Song): Long

    @Update
    suspend fun update(song: Song)

    @Delete
    suspend fun delete(song: Song)

    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getById(id: Long): Song?
}
