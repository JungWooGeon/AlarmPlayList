package com.sample.alarmplaylist.playlist.playlist_db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlayList::class], version = 1)
abstract class PlayListDataBase : RoomDatabase() {
    abstract fun playListDao() : PlayListDao
}