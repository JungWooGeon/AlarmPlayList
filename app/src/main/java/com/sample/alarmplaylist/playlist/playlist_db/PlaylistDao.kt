package com.sample.alarmplaylist.playlist.playlist_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlist")
    fun getAll(): List<Playlist>

    @Insert
    fun insertPlayList(playList: Playlist)

    @Delete
    fun deletePlayList(playList: Playlist)

    @Update
    fun updatePlayList(playList: Playlist)
}