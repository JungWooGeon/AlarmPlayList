package com.sample.alarmplaylist.playlist.playlist_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayListDao {
    @Query("SELECT * FROM playlist")
    fun getAll(): List<PlayList>

    @Insert
    fun insertPlayList(playList: PlayList)

    @Delete
    fun deletePlayList(playList: PlayList)

    @Update
    fun updatePlayList(playList: PlayList)
}