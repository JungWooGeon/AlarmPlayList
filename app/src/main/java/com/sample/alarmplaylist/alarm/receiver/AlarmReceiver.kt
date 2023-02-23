package com.sample.alarmplaylist.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1000"
        const val ALARM_URI = "알람음"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra(ALARM_URI, intent.getParcelableExtra<Uri>(ALARM_URI))
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        }
        else{
            context.startService(serviceIntent)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            context.stopService(serviceIntent)
        }, 3000)
    }
}