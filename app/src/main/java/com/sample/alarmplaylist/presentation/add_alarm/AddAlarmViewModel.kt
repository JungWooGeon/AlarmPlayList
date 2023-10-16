package com.sample.alarmplaylist.presentation.add_alarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.alarmplaylist.data.entity.Alarm
import com.sample.alarmplaylist.domain.alarm.AddAlarmUseCase
import com.sample.alarmplaylist.domain.alarm.GetLastAlarmUseCase
import com.sample.alarmplaylist.domain.alarm.UpdateAlarmUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddAlarmViewModel(
    private val addAlarmUseCase: AddAlarmUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val getLastAlarmUseCase: GetLastAlarmUseCase
): ViewModel() {

    val finishAddOrUpdateJob = MutableLiveData<Alarm>()

    fun addAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            addAlarmUseCase(alarm)
            val lastAlarm = getLastAlarmUseCase()
            finishAddOrUpdateJob.postValue(lastAlarm)
        }
    }

    fun updateAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            updateAlarmUseCase(alarm)
            finishAddOrUpdateJob.postValue(alarm)
        }
    }
}