package com.sample.alarmplaylist.alarm_db

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
data class Alarm (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "alarmHour") val alarmHour: String,
    @ColumnInfo(name = "alarmMinute") val alarmMinute: String
)