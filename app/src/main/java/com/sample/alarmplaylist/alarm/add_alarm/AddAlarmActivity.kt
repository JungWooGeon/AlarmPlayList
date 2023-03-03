package com.sample.alarmplaylist.alarm.add_alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.sample.alarmplaylist.MainActivity
import com.sample.alarmplaylist.alarm.AlarmFragment
import com.sample.alarmplaylist.alarm.alarm_db.Alarm
import com.sample.alarmplaylist.alarm.alarm_db.AlarmDataBase
import com.sample.alarmplaylist.databinding.ActivityAddAlarmBinding
import com.sample.alarmplaylist.alarm.AlarmFragment.Companion.ALARM_DB
import com.sample.alarmplaylist.alarm.dialog.SelectPlaylistDialog
import com.sample.alarmplaylist.alarm.receiver.AlarmReceiver
import java.util.*

class AddAlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAlarmBinding

    private var id: Int? = null
    private var playlistId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getIntExtra(AlarmFragment.INTENT_ALARM_ID, -1)
        val hour = intent.getStringExtra(AlarmFragment.INTENT_ALARM_HOUR)
        val minute = intent.getStringExtra(AlarmFragment.INTENT_ALARM_MINUTE)

        if (hour != null && minute != null) {
            binding.timePicker.hour = hour.toInt()
            binding.timePicker.minute = minute.toInt()
        }

        initButton()
        initViews()
    }

    private fun initButton() {
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            setResult(RESULT_CANCELED, intent)
            finish()
        }

        binding.btnSave.setOnClickListener {
            if (playlistId == -1) {
                Toast.makeText(this, "플레이리스트 설정을 다시 해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val db: AlarmDataBase =
                    Room.databaseBuilder(applicationContext, AlarmDataBase::class.java, ALARM_DB)
                        .build()

                val thread = if (id == -1) {
                    Thread {
                        db.alarmDao().insert(
                            Alarm(
                                null,
                                binding.timePicker.hour.toString(),
                                binding.timePicker.minute.toString(),
                                1,
                                playlistId,
                                binding.playlistName.text.toString()
                            )
                        )
                    }
                } else {
                    Thread {
                        db.alarmDao().updateAlarm(
                            Alarm(
                                id, binding.timePicker.hour.toString(),
                                binding.timePicker.minute.toString(),
                                1,
                                playlistId,
                                binding.playlistName.text.toString()
                            )
                        )
                    }
                }

                thread.start()
                thread.join()

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java).apply {
                    putExtra("alarmId", id)
                    putExtra("alarmHour", binding.timePicker.hour.toString())
                    putExtra("alarmMinute", binding.timePicker.minute.toString())
                    putExtra("playlistId", playlistId)
                    putExtra("playlistTitle", binding.playlistName.text)
                }
                val pendingIntent = PendingIntent.getBroadcast(this, id!!, intent, AlarmFragment.ALARM_FLAG)

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
                    set(Calendar.MINUTE, binding.timePicker.minute)

                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1)
                    }
                }

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                Toast.makeText(this, "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }

    private fun initViews() {
        binding.alarmMusicLayout.setOnClickListener {
            val dialog = SelectPlaylistDialog((object: SelectPlaylistDialog.DialogListener {
                override fun setPlaylist(playListId: Int, playListTitle: String) {
                    binding.playlistName.text = playListTitle
                    playlistId = playListId
                }
            }))
            // 알림창이 띄워져있는 동안 배경 클릭 막기
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, "RenamePlayListDialog")
        }
    }
}