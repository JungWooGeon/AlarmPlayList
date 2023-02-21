package com.sample.alarmplaylist.alarm.alarm_db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1)
abstract class AlarmDataBase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}