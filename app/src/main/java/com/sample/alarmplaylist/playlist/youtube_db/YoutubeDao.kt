package com.sample.alarmplaylist.playlist.youtube_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface YoutubeDao {
    @Query("SELECT * FROM youtube")
    fun getAll() : List<Youtube>

    @Insert
    fun insert(youtube: Youtube)

    @Delete
    fun delete(youtube: Youtube)

    @Query("DELETE FROM youtube WHERE playlistId == :id")
    fun deleteSelected(id: Int)

    @Query("SELECT * FROM youtube WHERE playlistId == :id")
    fun getSelected(id: Int) : List<Youtube>
}