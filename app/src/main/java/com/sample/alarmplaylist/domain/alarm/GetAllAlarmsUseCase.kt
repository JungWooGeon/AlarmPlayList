package com.sample.alarmplaylist.domain.alarm

import com.sample.alarmplaylist.data.entity.Alarm
import com.sample.alarmplaylist.data.repository.alarm.AlarmRepository

class GetAllAlarmsUseCase(private val repository: AlarmRepository) {
    suspend operator fun invoke(): List<Alarm> {
        return repository.getAllAlarms()
    }
}