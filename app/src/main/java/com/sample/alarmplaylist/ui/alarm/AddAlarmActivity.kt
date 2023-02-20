package com.sample.alarmplaylist.ui.alarm

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.sample.alarmplaylist.alarm_db.Alarm
import com.sample.alarmplaylist.alarm_db.AlarmDataBase
import com.sample.alarmplaylist.databinding.ActivityAddAlarmBinding
import com.sample.alarmplaylist.ui.alarm.AlarmFragment.Companion.ALARM_DB

class AddAlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val hour = intent.getStringExtra(AlarmFragment.INTENT_ALARM_HOUR)
        val minute = intent.getStringExtra(AlarmFragment.INTENT_ALARM_MINUTE)

        if (hour != null && minute != null) {
            binding.timePicker.hour = hour.toInt()
            binding.timePicker.minute = minute.toInt()
        }

        initButton()
    }

    private fun initButton() {
        binding.btnCancel.setOnClickListener{
            finish()
        }

        binding.btnSave.setOnClickListener {
            val db : AlarmDataBase = Room.databaseBuilder(applicationContext, AlarmDataBase::class.java, ALARM_DB).build()

            val thread = Thread {
                db.alarmDao().insert(
                    Alarm(null,
                        binding.timePicker.hour.toString(),
                        binding.timePicker.minute.toString()
                    )
                )
            }

            thread.start()
            thread.join()

            finish()
        }
    }
}