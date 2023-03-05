package com.sample.alarmplaylist

import android.app.PendingIntent
import android.os.Build

object Constant {
    const val ALARM_DB = "alarmDB"
    const val ALARM_HOUR = "alarmHour"
    const val ALARM_MINUTE = "alarmMinute"
    const val ALARM_ID = "alarmId"
    const val PLAYLIST_ID = "playlistId"
    const val PLAYLIST_TITLE = "playlistTitle"

    const val ALARM_ON = 1
    const val ALARM_OFF = 0

    const val PLAYLIST_DB = "playListDB"

    const val NONE_PLAYLIST_ID = -1
    const val NONE_PLAYLIST_TITLE = ""
    const val NONE_ALARM_ID = -1

    const val CALENDAR_ADD_AMOUNT = 1

    val ALARM_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
}