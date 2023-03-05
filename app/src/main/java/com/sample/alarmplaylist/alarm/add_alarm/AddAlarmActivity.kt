package com.sample.alarmplaylist.alarm.add_alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sample.alarmplaylist.Constant
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.databinding.ActivityAddAlarmBinding
import com.sample.alarmplaylist.alarm.dialog.SelectPlaylistDialog
import com.sample.alarmplaylist.alarm.notification.AlarmReceiver
import java.util.*

/**
 * 알람 정보를 수정하거나 추가할 수 있는 Activity 로 알람 정보/수정을 완료하면 종료된다.
 */
class AddAlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAlarmBinding
    private lateinit var viewModel: AddAlarmViewModel

    private var alarmId: Int? = null
    private var playlistId = Constant.NONE_PLAYLIST_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[AddAlarmViewModel::class.java]

        initViews()
        initButton()
    }

    private fun initButton() {
        // 취소 버튼 클릭 시 activity 종료
        binding.btnCancel.setOnClickListener { finish() }

        // 저장 버튼 클릭 시 db 저장, 알람 설정 후 activity 종료
        binding.btnSave.setOnClickListener {
            if (playlistId == -1) {
                Toast.makeText(this, getString(R.string.reset_playlist_setting), Toast.LENGTH_SHORT).show()
            } else {
                // db 저장
                if (alarmId == -1) {
                    viewModel.addAlarm(applicationContext, binding.timePicker.hour.toString(),
                        binding.timePicker.minute.toString(), playlistId, binding.playlistName.text.toString())
                } else {
                    viewModel.updateAlarm(applicationContext, alarmId!!, binding.timePicker.hour.toString(),
                        binding.timePicker.minute.toString(), playlistId, binding.playlistName.text.toString())
                }

                // 알람 설정
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java).apply {
                    putExtra(Constant.ALARM_ID, alarmId)
                    putExtra(Constant.ALARM_HOUR, binding.timePicker.hour.toString())
                    putExtra(Constant.ALARM_MINUTE, binding.timePicker.minute.toString())
                    putExtra(Constant.PLAYLIST_ID, playlistId)
                    putExtra(Constant.PLAYLIST_TITLE, binding.playlistName.text)
                }
                val pendingIntent = PendingIntent.getBroadcast(this, alarmId!!, intent, Constant.ALARM_FLAG)

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
                    set(Calendar.MINUTE, binding.timePicker.minute)

                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, Constant.CALENDAR_ADD_AMOUNT)
                    }
                }

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

                Toast.makeText(this, getString(R.string.complete_add_alarm), Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }

    private fun initViews() {
        alarmId = intent.getIntExtra(Constant.ALARM_ID, Constant.NONE_ALARM_ID)
        val hour = intent.getStringExtra(Constant.ALARM_HOUR)
        val minute = intent.getStringExtra(Constant.ALARM_MINUTE)

        // 알람 변경일 경우 timePicker 에 설정되어 있던 시간 셋팅
        if (hour != null && minute != null) {
            binding.timePicker.hour = hour.toInt()
            binding.timePicker.minute = minute.toInt()
        }

        // 알람음 설정 layout click event, listener 설정 후, SelectPlaylistDialog 띄우기
        binding.alarmMusicLayout.setOnClickListener {
            val dialog = SelectPlaylistDialog((object: SelectPlaylistDialog.DialogListener {
                override fun setPlaylist(playListId: Int, playListTitle: String) {
                    binding.playlistName.text = playListTitle
                    playlistId = playListId
                }
            }))
            // 알림창이 띄워져있는 동안 배경 클릭 막기
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, getString(R.string.select_playlist_dialog))
        }
    }
}