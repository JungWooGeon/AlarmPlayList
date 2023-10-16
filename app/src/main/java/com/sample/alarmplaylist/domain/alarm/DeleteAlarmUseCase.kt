package com.sample.alarmplaylist.domain.alarm

import com.sample.alarmplaylist.data.repository.alarm.AlarmRepository

class DeleteAlarmUseCase(private val repository: AlarmRepository) {
    suspend operator fun invoke(id: Int) {
        repository.deleteAlarm(id)
    }
}