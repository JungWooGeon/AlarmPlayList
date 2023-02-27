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
import com.sample.alarmplaylist.alarm.receiver.AlarmReceiver
import java.util.*

class AddAlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAlarmBinding
    private var id: Int? = null

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
    }

    private fun initButton() {
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            setResult(RESULT_CANCELED, intent)
            finish()
        }

        binding.btnSave.setOnClickListener {
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
                            1
                        )
                    )
                }
            } else {
                Thread {
                    db.alarmDao().updateAlarm(
                        Alarm(
                            id, binding.timePicker.hour.toString(),
                            binding.timePicker.minute.toString(),
                            1
                        )
                    )
                }
            }

            thread.start()
            thread.join()

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmReceiver::class.java).apply {
                putExtra("id", id)
                putExtra("hour", binding.timePicker.hour.toString())
                putExtra("minute", binding.timePicker.minute.toString())
                putExtra("알람음", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
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