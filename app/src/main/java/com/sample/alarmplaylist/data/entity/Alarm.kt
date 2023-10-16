package com.sample.alarmplaylist.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "alarmHour") val alarmHour: String,
    @ColumnInfo(name = "alarmMinute") val alarmMinute: String,
    @ColumnInfo(name = "onOff") var onOff: Boolean,
    @ColumnInfo(name = "playlistId") val playlistId: Int,
    @ColumnInfo(name = "playlistName") val playlistName: String
)