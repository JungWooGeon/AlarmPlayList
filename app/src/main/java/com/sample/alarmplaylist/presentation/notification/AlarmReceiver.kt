package com.sample.alarmplaylist.presentation.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sample.alarmplaylist.Constants

/**
 * 정시에 시작되는 알람으로 실행되며, 알람 시작 시 AlarmService 실행
 */
class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra(Constants.ALARM_ID, intent.getIntExtra(Constants.ALARM_ID, -1))
            putExtra(Constants.ALARM_HOUR, intent.getStringExtra(Constants.ALARM_HOUR))
            putExtra(Constants.ALARM_MINUTE, intent.getStringExtra(Constants.ALARM_MINUTE))
            putExtra(Constants.PLAYLIST_ID, intent.getIntExtra(Constants.PLAYLIST_ID, -1))
            putExtra(Constants.PLAYLIST_TITLE, intent.getStringExtra(Constants.PLAYLIST_TITLE))
        }

        context.startService(serviceIntent)
    }
}