package com.sample.alarmplaylist.data.db.playlist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sample.alarmplaylist.data.entity.Playlist

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlist")
    fun getAll(): List<Playlist>

    @Insert
    fun insertPlayList(playList: Playlist)

    @Query("DELETE FROM playlist WHERE id = :id")
    fun deletePlaylistById(id: Int)

    @Update
    fun updatePlayList(playList: Playlist)
}