package com.sample.alarmplaylist.alarm.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sample.alarmplaylist.Constant

/**
 * 정시에 시작되는 알람으로 실행되며, 알람 시작 시 AlarmService 실행
 */
class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra(Constant.ALARM_ID, intent.getIntExtra(Constant.ALARM_ID, -1))
            putExtra(Constant.ALARM_HOUR, intent.getStringExtra(Constant.ALARM_HOUR))
            putExtra(Constant.ALARM_MINUTE, intent.getStringExtra(Constant.ALARM_MINUTE))
            putExtra(Constant.PLAYLIST_ID, intent.getIntExtra(Constant.PLAYLIST_ID, -1))
            putExtra(Constant.PLAYLIST_TITLE, intent.getStringExtra(Constant.PLAYLIST_TITLE))
        }

        context.startService(serviceIntent)
    }
}