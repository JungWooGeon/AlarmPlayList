package com.sample.alarmplaylist.data.repository.alarm

import com.sample.alarmplaylist.data.db.alarm.AlarmDataBase
import com.sample.alarmplaylist.data.entity.Alarm

class AlarmRepositoryImpl(private val db: AlarmDataBase) : AlarmRepository {

    override suspend fun deleteAlarm(id: Int) {
        db.alarmDao().deleteAlarmById(id)
    }

    override suspend fun getAllAlarms(): List<Alarm> {
        return db.alarmDao().getAll()
    }

    override suspend fun updateAlarm(alarm: Alarm) {
        return db.alarmDao().updateAlarm(alarm)
    }

    override suspend fun addAlarm(alarm: Alarm) {
        return db.alarmDao().insert(alarm)
    }

    override suspend fun getLastAlarm(): Alarm {
        return db.alarmDao().getLast()[0]
    }
}