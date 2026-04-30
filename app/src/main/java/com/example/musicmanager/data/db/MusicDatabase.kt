package com.example.musicmanager.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musicmanager.data.dao.SongDao
import com.example.musicmanager.data.model.Song

@Database(entities = [Song::class, Playlist::class, PlaylistSongCrossRef::class], version = 2, exportSchema = false)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
}
