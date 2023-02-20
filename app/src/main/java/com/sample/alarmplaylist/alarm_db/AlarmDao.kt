package com.sample.alarmplaylist.alarm_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll() : List<Alarm>

    @Insert
    fun insert(alarm: Alarm)

    @Delete
    fun deleteAlarm(alarm: Alarm)
}