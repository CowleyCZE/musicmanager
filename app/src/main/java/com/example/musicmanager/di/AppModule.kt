package com.example.musicmanager.di

import android.content.Context
import androidx.room.Room
import com.example.musicmanager.data.dao.SongDao
import com.example.musicmanager.data.dao.PlaylistDao
import com.example.musicmanager.data.db.MusicDatabase
import com.example.musicmanager.data.repository.SongRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MusicDatabase =
        Room.databaseBuilder(
            context,
            MusicDatabase::class.java,
            "music-db"
        ).fallbackToDestructiveMigration()
         .build()

    @Provides
    fun provideSongDao(db: MusicDatabase): SongDao = db.songDao()

    @Provides
    fun providePlaylistDao(db: MusicDatabase): PlaylistDao = db.playlistDao()

    @Provides
    @Singleton
    fun provideRepository(songDao: SongDao): SongRepository = SongRepository(songDao)
}
