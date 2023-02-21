package com.sample.alarmplaylist.alarm

import android.content.Context
import androidx.room.Room
import com.sample.alarmplaylist.alarm.alarm_db.Alarm
import com.sample.alarmplaylist.alarm.alarm_db.AlarmDataBase

class AlarmModel {
    var list: List<Alarm>? = null
    val alarmList: ArrayList<String> = ArrayList()
    val onOffList: ArrayList<Boolean> = ArrayList()

    fun deleteAlarm(context: Context, id: Int) {
        val db : AlarmDataBase = Room.databaseBuilder(context, AlarmDataBase::class.java,
            AlarmFragment.ALARM_DB
        ).build()
        val thread = Thread {
            db.alarmDao().deleteAlarm(list!![id])
        }
        thread.start()
        thread.join()

        readAlarmData(context)
    }

    fun readAlarmData(context: Context) {
        alarmList.clear()
        onOffList.clear()

        // DB 에서 데이터를 읽어온 후 adapter 에 반영
        val db : AlarmDataBase = Room.databaseBuilder(context, AlarmDataBase::class.java,
            AlarmFragment.ALARM_DB
        ).build()
        val thread = Thread {
            list = db.alarmDao().getAll()

            list?.forEach {
                var result = if (it.alarmHour.toInt() >= 12) { "오후 " } else { "오전 " }
                result += if (it.alarmHour.toInt() == 0 || it.alarmHour.toInt() == 12) {
                    "12"
                } else if ((it.alarmHour.toInt() % 12) < 10) {
                    "0" + (it.alarmHour.toInt() % 12).toString()
                } else {
                    (it.alarmHour.toInt() % 12).toString()
                }
                result += if (it.alarmMinute.toInt() < 10) {
                    ":" + "0" + it.alarmMinute
                } else {
                    ":" + it.alarmMinute
                }

                alarmList.add(result)
                onOffList.add(it.onOff == 1)
            }
        }

        thread.start()
        thread.join()
    }

    fun setCheckedChange(context: Context, pos: Int, isChecked: Boolean) {
        val db : AlarmDataBase = Room.databaseBuilder(context, AlarmDataBase::class.java,
            AlarmFragment.ALARM_DB
        ).build()

        val thread = Thread {
            db.alarmDao().updateAlarm(
                Alarm(
                    list?.get(pos)?.id, list?.get(pos)?.alarmHour!!,
                    list?.get(pos)?.alarmMinute!!,
                    if (isChecked) { 1 } else { 0 }
                )
            )
        }

        thread.start()
        thread.join()

        readAlarmData(context)
    }
}