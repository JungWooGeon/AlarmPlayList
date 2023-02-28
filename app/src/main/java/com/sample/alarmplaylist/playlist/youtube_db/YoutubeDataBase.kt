package com.sample.alarmplaylist.playlist.youtube_db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Youtube::class], version = 1)
abstract class YoutubeDataBase : RoomDatabase() {
    abstract fun youtubeDao() : YoutubeDao
}