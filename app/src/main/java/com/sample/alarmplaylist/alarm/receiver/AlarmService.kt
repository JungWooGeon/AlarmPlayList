package com.sample.alarmplaylist.alarm.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sample.alarmplaylist.MainActivity
import com.sample.alarmplaylist.R

class AlarmService : Service() {

    companion object {
        val ALARM_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }

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
        //mp = MediaPlayer.create(this, intent?.getParcelableExtra(AlarmReceiver.ALARM_URI)!!)
        mp = MediaPlayer.create(this, R.raw.test)
        mp.setOnCompletionListener {
            stopSelf()
        }
        mp.start()

        notifyNotification(this, intent!!.getIntExtra("id", -1))

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

    private fun notifyNotification(context: Context, id: Int) {
        // @TODO SharedPreferences 에 실행되는 id 추가 하기
        val prefs = context.getSharedPreferences("playAlarmId", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString("id", id.toString())
            putBoolean("onPlay", true)
            apply()
        }

        val intent = Intent(context, MainActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = Intent.ACTION_MAIN
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, ALARM_FLAG)

        val builder = NotificationCompat.Builder(context, AlarmReceiver.NOTIFICATION_CHANNEL_ID).apply {
                setContentTitle("알람")
                setContentText("일어날 시간입니다.")
                setSmallIcon(R.drawable.ic_launcher_foreground)
                priority = NotificationCompat.PRIORITY_HIGH
                setContentIntent(pendingIntent)
            }

        startForeground(1, builder.build())
    }
}