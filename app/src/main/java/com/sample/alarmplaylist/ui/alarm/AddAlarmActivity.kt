package com.sample.alarmplaylist.ui.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sample.alarmplaylist.databinding.ActivityAddAlarmBinding

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
    }
}