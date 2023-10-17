package com.sample.alarmplaylist.presentation.alarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.alarmplaylist.data.entity.Alarm
import com.sample.alarmplaylist.domain.alarm.DeleteAlarmUseCase
import com.sample.alarmplaylist.domain.alarm.GetAllAlarmsUseCase
import com.sample.alarmplaylist.domain.alarm.UpdateAlarmUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val getAllAlarmsUseCase: GetAllAlarmsUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase
): ViewModel() {

    val alarmList = MutableLiveData<List<Alarm>>()
    val setAlarmOff = MutableLiveData<Alarm>()

    fun deleteAlarmAt(position: Int) {
        val id = alarmList.value?.get(position)?.id

        if (id != null) {
            viewModelScope.launch(Dispatchers.IO) {
                setAlarmOff.postValue(alarmList.value?.get(position))
                deleteAlarmUseCase(id)
                loadAlarms()  // 삭제 후, 알람 리스트 갱신
            }
        }
    }

    fun loadAlarms() {
        viewModelScope.launch(Dispatchers.IO) {
            val alarms = getAllAlarmsUseCase()
            alarmList.postValue(alarms)
        }
    }

    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            updateAlarmUseCase(alarm)
            loadAlarms()  // 업데이트 후, 알람 리스트 갱신
        }
    }
}