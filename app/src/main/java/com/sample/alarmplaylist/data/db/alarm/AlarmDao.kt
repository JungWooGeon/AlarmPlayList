package com.sample.alarmplaylist.data.db.alarm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sample.alarmplaylist.data.entity.Alarm

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll() : List<Alarm>

    @Insert
    fun insert(alarm: Alarm)

    @Delete
    fun deleteAlarm(alarm: Alarm)

    @Query("DELETE FROM alarm WHERE id = :id")
    fun deleteAlarmById(id: Int)

    @Update
    fun updateAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarm ORDER BY id DESC LIMIT 1")
    fun getLast() : List<Alarm>
}