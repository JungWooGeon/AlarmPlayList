package com.sample.alarmplaylist.alarm

import android.content.Context
import androidx.room.Room
import com.sample.alarmplaylist.Constant
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.alarm.alarm_db.Alarm
import com.sample.alarmplaylist.alarm.alarm_db.AlarmDataBase

/**
 * AlarmViewModel Model
 * 1. list : 알람 정보 저장
 * 2. alarmList : 가공된 알람 정보 저장
 * 3. onOffList : 가공된 알람 스위치 정보 저장
 * 4. deleteAlarm : DB 에서 알람 삭제 후 정보 업데이트
 * 5. readAlarmData : DB 에서 알람 정보 조회
 * 6. setCheckedChange : DB 에서 알람 스위치 정보 변경
 */
class AlarmModel {

    companion object {
        private const val ALARM_TWELVE = 12
        private const val ALARM_TEN = 10
        private const val ALARM_ZERO = 0
    }

    var list: List<Alarm>? = null
    val alarmList: ArrayList<String> = ArrayList()
    val onOffList: ArrayList<Boolean> = ArrayList()

    fun deleteAlarm(context: Context, id: Int) {
        val db : AlarmDataBase = Room.databaseBuilder(context, AlarmDataBase::class.java, Constant.ALARM_DB).build()

        val thread = Thread { db.alarmDao().deleteAlarm(list!![id]) }
        thread.start()
        thread.join()

        readAlarmData(context)
    }

    fun readAlarmData(context: Context) {
        alarmList.clear()
        onOffList.clear()

        val db : AlarmDataBase = Room.databaseBuilder(context, AlarmDataBase::class.java, Constant.ALARM_DB).build()

        val thread = Thread {
            list = db.alarmDao().getAll()

            // 알람 정보에서 데이터를 가공하여 알맞게 저장
            list?.forEach {
                var result = if (it.alarmHour.toInt() >= ALARM_TWELVE) { context.getString(R.string.pm) } else { context.getString(R.string.am) }
                result += if (it.alarmHour.toInt() == ALARM_ZERO || it.alarmHour.toInt() == ALARM_TWELVE) {
                    context.getString(R.string.twelve)
                } else if ((it.alarmHour.toInt() % ALARM_TWELVE) < ALARM_TEN) {
                    context.getString(R.string.zero) + (it.alarmHour.toInt() % ALARM_TWELVE).toString()
                } else {
                    (it.alarmHour.toInt() % ALARM_TWELVE).toString()
                }
                result += if (it.alarmMinute.toInt() < ALARM_TEN) {
                    context.getString(R.string.colon) + context.getString(R.string.zero) + it.alarmMinute
                } else {
                    context.getString(R.string.colon) + it.alarmMinute
                }

                alarmList.add(result)
                onOffList.add(it.onOff == Constant.ALARM_ON)
            }
        }

        thread.start()
        thread.join()
    }

    fun setCheckedChange(context: Context, pos: Int, isChecked: Boolean) {
        val db : AlarmDataBase = Room.databaseBuilder(context, AlarmDataBase::class.java, Constant.ALARM_DB).build()

        val thread = Thread {
            db.alarmDao().updateAlarm(
                Alarm(
                    list?.get(pos)?.id, list?.get(pos)?.alarmHour!!,
                    list?.get(pos)?.alarmMinute!!,
                    if (isChecked) { Constant.ALARM_ON } else { Constant.ALARM_OFF },
                    list?.get(pos)?.playlistId!!,
                    list?.get(pos)?.playlistName!!
                )
            )
        }

        thread.start()
        thread.join()

        readAlarmData(context)
    }
}