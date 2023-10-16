package com.sample.alarmplaylist

import android.app.PendingIntent
import android.os.Build

/**
 * 전역 상수 모음
 */
object Constants {
    const val ALARM_DB = "alarmDB"
    const val ALARM_HOUR = "alarmHour"
    const val ALARM_MINUTE = "alarmMinute"
    const val ALARM_ID = "alarmId"
    const val PLAYLIST_ID = "playlistId"
    const val PLAYLIST_TITLE = "playlistTitle"

    const val PLAYLIST_DB = "playListDB"
    const val YOUTUBE_DB = "YoutubeDB"

    const val NONE_PLAYLIST_ID = -1
    const val NONE_PLAYLIST_TITLE = ""
    const val NONE_ALARM_ID = -1

    const val CALENDAR_ADD_AMOUNT = 1
    const val PLAYLIST_IMAGE_COUNT = 9

    const val DEFAULT_SELECTED_POSITION = 0

    const val YOUTUBE_PLAYER_VIEW_START_SECONDS = 0F

    val ALARM_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
}