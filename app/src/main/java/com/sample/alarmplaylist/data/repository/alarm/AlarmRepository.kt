package com.sample.alarmplaylist.data.repository.alarm

import com.sample.alarmplaylist.data.entity.Alarm

interface AlarmRepository {
    suspend fun deleteAlarm(id: Int)
    suspend fun getAllAlarms(): List<Alarm>
    suspend fun updateAlarm(alarm: Alarm)
    suspend fun addAlarm(alarm: Alarm)
    suspend fun getLastAlarm(): Alarm
}