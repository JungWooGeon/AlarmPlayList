package com.sample.alarmplaylist.presentation.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.data.entity.Alarm
import com.sample.alarmplaylist.data.db.alarm.AlarmDataBase
import com.sample.alarmplaylist.presentation.play_youtube.YoutubePlayActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 정시에 시작되는 AlarmReceiver 에서 실행이 되어 알람 notification 을 full screen 으로 보낸다.
 */
class AlarmService : Service() {

    companion object {
        private const val CHANNEL_NAME = "알람"
        private const val CHANNEL_DESCRIPTION = "알람 실행"
        private const val CHANNEL_ID = "Channel Id"
        private const val NOTIFICATION_ID = 114
        private const val INTENT_DEFAULT_VALUE = 1
        private const val PENDING_INTENT_REQUEST_CODE = 0
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setOffAlarm(intent!!)
        notifyNotification(intent)

        return START_NOT_STICKY
    }

    // notification channel 생성
    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.description = CHANNEL_DESCRIPTION

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
    }

    // notification full screen 으로 띄우기
    private fun notifyNotification(intent: Intent) {
        val playYoutubeIntent = Intent(this, YoutubePlayActivity::class.java).apply {
            putExtra(Constants.PLAYLIST_ID, intent.getIntExtra(Constants.PLAYLIST_ID, INTENT_DEFAULT_VALUE))
        }

        val pendingIntent = PendingIntent.getActivity(this, PENDING_INTENT_REQUEST_CODE, playYoutubeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_alarm)
            setContentTitle(getString(R.string.alarm))
            setContentText(getString(R.string.switch_alarm_activity))
            priority = NotificationCompat.PRIORITY_HIGH
            setCategory(NotificationCompat.CATEGORY_ALARM)
            setAutoCancel(true)
            setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.switch_alarm_activity)))
            setFullScreenIntent(pendingIntent, true)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build())
    }

    // DB 알람 off 로 설정
    private fun setOffAlarm(intent: Intent) {
        // 백그라운드에서 실행될 사용자 정의 Scope
        CoroutineScope(Dispatchers.IO).launch {
            // 백그라운드 작업
            val db : AlarmDataBase = Room.databaseBuilder(applicationContext, AlarmDataBase::class.java,
                Constants.ALARM_DB
            ).build()
            db.alarmDao().updateAlarm(
                Alarm(
                    intent.getIntExtra(Constants.ALARM_ID, INTENT_DEFAULT_VALUE),
                    intent.getStringExtra(Constants.ALARM_HOUR).toString(),
                    intent.getStringExtra(Constants.ALARM_MINUTE).toString(),
                    false,
                    intent.getIntExtra(Constants.PLAYLIST_ID, INTENT_DEFAULT_VALUE),
                    intent.getStringExtra(Constants.PLAYLIST_TITLE).toString()
                )
            )
        }
    }
}