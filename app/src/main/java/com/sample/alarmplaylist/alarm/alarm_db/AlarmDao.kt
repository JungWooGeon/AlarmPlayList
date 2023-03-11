package com.sample.alarmplaylist.alarm.alarm_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll() : List<Alarm>

    @Insert
    fun insert(alarm: Alarm)

    @Delete
    fun deleteAlarm(alarm: Alarm)

    @Update
    fun updateAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarm ORDER BY id DESC LIMIT 1")
    fun getLast() : List<Alarm>
}