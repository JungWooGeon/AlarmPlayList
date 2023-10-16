package com.sample.alarmplaylist.domain.alarm

import com.sample.alarmplaylist.data.entity.Alarm
import com.sample.alarmplaylist.data.repository.alarm.AlarmRepository

class AddAlarmUseCase(private val repository: AlarmRepository) {
    suspend operator fun invoke(alarm: Alarm) {
        repository.addAlarm(alarm)
    }
}