package com.sample.alarmplaylist.alarm.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.appcompat.resources.Compatibility.Api18Impl.setAutoCancel
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.alarm.AlarmFragment
import com.sample.alarmplaylist.alarm.alarm_db.Alarm
import com.sample.alarmplaylist.alarm.alarm_db.AlarmDataBase
import com.sample.alarmplaylist.playlist.play_youtube.YoutubePlayActivity

// @TODO
// 1. custom notification 으로 youtubeplayerview를 띄워보기... -> 이것마저 안되면 그냥 play store 포기하고, background player로 도전
// 2. 알람 실행 시 DB 에서 알람 끄기로 전환

class AlarmService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setOffAlarm(intent!!)

        val playYoutubeIntent = Intent(this, YoutubePlayActivity::class.java).apply {
            putExtra("playlistId", intent.getIntExtra("playlistId", -1))
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            playYoutubeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        ).apply {
            setSmallIcon(R.drawable.ic_alarm)
            setContentTitle("알람")
            setContentText("알람이 시작되었습니다")
            priority = NotificationCompat.PRIORITY_HIGH
            setCategory(NotificationCompat.CATEGORY_ALARM)
            setAutoCancel(true)
            setStyle(NotificationCompat.BigTextStyle().bigText("알람이 시작되었습니다"))
            setFullScreenIntent(pendingIntent, true)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        }

        NotificationManagerCompat.from(this).notify(114, builder.build())

        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun setOffAlarm(intent: Intent) {
        val db : AlarmDataBase = Room.databaseBuilder(applicationContext, AlarmDataBase::class.java,
            AlarmFragment.ALARM_DB
        ).build()

        val thread = Thread {
            db.alarmDao().updateAlarm(
                Alarm(
                    intent.getIntExtra("alarmId", -1),
                    intent.getStringExtra("alarmHour").toString(),
                    intent.getStringExtra("alarmMinute").toString(),
                    0,
                    intent.getIntExtra("playlistId", -1),
                    intent.getStringExtra("playlistTitle").toString()
                )
            )
        }

        thread.start()
        thread.join()
    }

    companion object {
        private const val CHANNEL_NAME = "알람"
        private const val CHANNEL_DESCRIPTION = "알람 실행"
        private const val CHANNEL_ID = "Channel Id"
    }
}