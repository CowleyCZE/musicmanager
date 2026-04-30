package com.example.musicmanager.data.dao

import androidx.room.*
import com.example.musicmanager.data.model.Playlist
import com.example.musicmanager.data.model.PlaylistSongCrossRef
import com.example.musicmanager.data.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<Playlist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongToPlaylist(crossRef: PlaylistSongCrossRef)

    @Transaction
    @Query("""
        SELECT songs.* FROM songs 
        INNER JOIN playlist_song_cross_ref ON songs.id = playlist_song_cross_ref.songId 
        WHERE playlist_song_cross_ref.playlistId = :playlistId
    """)
    fun getSongsInPlaylist(playlistId: Long): Flow<List<Song>>

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)
}
