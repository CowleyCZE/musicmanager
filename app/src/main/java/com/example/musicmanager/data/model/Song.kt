package com.example.musicmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val audioUri: String,
    val coverUri: String? = null,
    val lyrics: String,
    val musicDescription: String,
    val sourceTag: String,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
