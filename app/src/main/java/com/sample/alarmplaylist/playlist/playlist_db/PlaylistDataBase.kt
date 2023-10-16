package com.sample.alarmplaylist.playlist.playlist_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sample.alarmplaylist.Constants

@Database(entities = [Playlist::class], version = 1)
abstract class PlaylistDataBase : RoomDatabase() {
    abstract fun playListDao() : PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: PlaylistDataBase? = null

        fun getInstance(context: Context): PlaylistDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PlaylistDataBase::class.java, Constants.PLAYLIST_DB
            ).build()
    }
}