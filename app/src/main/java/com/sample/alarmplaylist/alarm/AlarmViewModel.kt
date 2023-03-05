package com.sample.alarmplaylist.alarm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.alarmplaylist.alarm.alarm_db.Alarm

/**
 * AlarmFragment ViewModel
 * 1. getAlarmInfo : 알람 정보 요청
 * 2. deleteAlarm : 알람 삭제 요청
 * 3. readAlarmData : DB 에서 알람 정보 조회 요청
 * 4. getOnOffList : 가공된 알람 스위치 정보 요청
 * 5. setCheckedChange : DB 알람 스위치 정보 변경 요청
 */
class AlarmViewModel: ViewModel() {

    private val model: AlarmModel = AlarmModel()
    val alarmList = MutableLiveData<List<String>>()

    fun getAlarmInfo(): List<Alarm>? {
        return model.list
    }

    fun deleteAlarm(context: Context, id: Int) {
        model.deleteAlarm(context, id)
        alarmList.value = model.alarmList
    }

    fun readAlarmData(context: Context) {
        model.readAlarmData(context)
        alarmList.value = model.alarmList
    }

    fun getOnOffList(): ArrayList<Boolean> {
        return model.onOffList
    }

    fun setCheckedChange(context: Context, pos: Int, isChecked: Boolean) {
        model.setCheckedChange(context, pos, isChecked)
    }
}