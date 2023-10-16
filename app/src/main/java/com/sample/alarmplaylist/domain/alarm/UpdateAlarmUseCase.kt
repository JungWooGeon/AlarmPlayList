package com.sample.alarmplaylist.domain.alarm

import com.sample.alarmplaylist.data.entity.Alarm
import com.sample.alarmplaylist.data.repository.alarm.AlarmRepository

class UpdateAlarmUseCase(private val repository: AlarmRepository) {
    suspend operator fun invoke(alarm: Alarm) {
        repository.updateAlarm(alarm)
    }
}