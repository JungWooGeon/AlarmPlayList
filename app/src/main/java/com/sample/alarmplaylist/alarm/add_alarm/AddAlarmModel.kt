package com.sample.alarmplaylist.alarm.add_alarm

import android.content.Context
import androidx.room.Room
import com.sample.alarmplaylist.Constant
import com.sample.alarmplaylist.alarm.alarm_db.Alarm
import com.sample.alarmplaylist.alarm.alarm_db.AlarmDataBase

/**
 * AddAlarmViewModel Model
 * 1. addAlarm : DB 에 알람 추가
 * 2. updateAlarm : DB 에서 알람 정보 수정
 */
class AddAlarmModel {
    fun addAlarm(context: Context, hour: String, minute: String, playlistId: Int, playlistName: String) : Int {
        var id = -1
        val db: AlarmDataBase = Room.databaseBuilder(context, AlarmDataBase::class.java, Constant.ALARM_DB).build()

        var thread = Thread {
            db.alarmDao().insert(Alarm(null, hour, minute, Constant.ALARM_ON, playlistId, playlistName))
        }

        thread.start()
        thread.join()

        thread = Thread { id = db.alarmDao().getLast()[0].id!! }
        thread.start()
        thread.join()

        return id
    }

    fun updateAlarm(context: Context, alarmId: Int, hour: String, minute: String, playlistId: Int, playlistName: String) {
        val db: AlarmDataBase = Room.databaseBuilder(context, AlarmDataBase::class.java, Constant.ALARM_DB).build()

        val thread = Thread {
            db.alarmDao().updateAlarm(Alarm(alarmId, hour, minute, Constant.ALARM_ON, playlistId, playlistName))
        }

        thread.start()
        thread.join()
    }
}