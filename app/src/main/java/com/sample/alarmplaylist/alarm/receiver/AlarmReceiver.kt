package com.sample.alarmplaylist.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("alarmId", intent.getIntExtra("alarmId", -1))
            putExtra("alarmHour", intent.getStringExtra("alarmHour"))
            putExtra("alarmMinute", intent.getStringExtra("alarmMinute"))
            putExtra("playlistId", intent.getIntExtra("playlistId", -1))
            putExtra("playlistTitle", intent.getStringExtra("playlistTitle"))
        }

        context.startService(serviceIntent)
    }
}