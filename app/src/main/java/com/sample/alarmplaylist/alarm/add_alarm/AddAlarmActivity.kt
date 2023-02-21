package com.sample.alarmplaylist.alarm.add_alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.sample.alarmplaylist.alarm.AlarmFragment
import com.sample.alarmplaylist.alarm.alarm_db.Alarm
import com.sample.alarmplaylist.alarm.alarm_db.AlarmDataBase
import com.sample.alarmplaylist.databinding.ActivityAddAlarmBinding
import com.sample.alarmplaylist.alarm.AlarmFragment.Companion.ALARM_DB

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
                val onOff = intent.getBooleanExtra(AlarmFragment.INTENT_ALARM_ON_OFF, true)
                Thread {
                    db.alarmDao().updateAlarm(
                        Alarm(
                            id, binding.timePicker.hour.toString(),
                            binding.timePicker.minute.toString(),
                            if (onOff) { 1 } else { 0 }
                        )
                    )
                }
            }

            thread.start()
            thread.join()

            finish()
        }
    }
}