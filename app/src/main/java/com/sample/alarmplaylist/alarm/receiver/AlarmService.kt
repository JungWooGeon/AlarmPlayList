package com.sample.alarmplaylist.alarm.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sample.alarmplaylist.R

class AlarmService : Service() {

    private lateinit var mp: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mp.stop()
        mp.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mp = MediaPlayer.create(this, intent?.getParcelableExtra(AlarmReceiver.ALARM_URI)!!)
        mp.start()
        notifyNotification(this)

        return START_STICKY
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                AlarmReceiver.NOTIFICATION_CHANNEL_ID,
                "기상 알람",
                NotificationManager.IMPORTANCE_LOW
            )

            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    private fun notifyNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, AlarmReceiver.NOTIFICATION_CHANNEL_ID).apply {
                setContentTitle("알람")
                setContentText("일어날 시간입니다.")
                setSmallIcon(R.drawable.ic_launcher_foreground)
                priority = NotificationCompat.PRIORITY_HIGH
                setAutoCancel(true)
            }

        startForeground(1, builder.build())
    }
}