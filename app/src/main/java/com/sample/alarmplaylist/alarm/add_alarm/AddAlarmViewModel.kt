package com.sample.alarmplaylist.alarm.add_alarm

import android.content.Context
import androidx.lifecycle.ViewModel

/**
 * AddAlarmActivity ViewModel
 * 1. addAlarm : DB 에 알람 추가 요청
 * 2. updateAlarm : DB 에 알람 정보 수정 요청
 */
class AddAlarmViewModel: ViewModel() {

    private val model = AddAlarmModel()

    fun addAlarm(context: Context, hour: String, minute: String, playlistId: Int, playlistName: String) {
        model.addAlarm(context, hour, minute, playlistId, playlistName)
    }

    fun updateAlarm(context: Context, alarmId: Int, hour: String, minute: String, playlistId: Int, playlistName: String) {
        model.updateAlarm(context, alarmId, hour, minute, playlistId, playlistName)
    }
}