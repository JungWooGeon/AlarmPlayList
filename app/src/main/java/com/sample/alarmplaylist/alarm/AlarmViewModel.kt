package com.sample.alarmplaylist.alarm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.alarmplaylist.alarm.alarm_db.Alarm

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