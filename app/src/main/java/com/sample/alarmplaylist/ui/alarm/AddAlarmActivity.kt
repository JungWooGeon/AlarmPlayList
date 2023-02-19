package com.sample.alarmplaylist.ui.alarm

import android.os.Bundle
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