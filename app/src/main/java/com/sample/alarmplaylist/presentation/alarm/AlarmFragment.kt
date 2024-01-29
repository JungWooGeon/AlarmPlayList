package com.sample.alarmplaylist.presentation.alarm

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.data.entity.Alarm
import com.sample.alarmplaylist.databinding.FragmentAlarmBinding
import com.sample.alarmplaylist.presentation.add_alarm.AddAlarmActivity
import com.sample.alarmplaylist.presentation.notification.AlarmReceiver
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar
import kotlin.system.exitProcess

/**
 * 알람 정보를 보여주는 화면이고, 알람을 추가하거나 변경할 수 있는 AddAlarmActivity 를 띄우는 역할을 한다.
 */
class AlarmFragment : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<AlarmViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 알람 화면에 보여지는 알람 정보가 변경될 경우 update
        viewModel.alarmList.observe(viewLifecycleOwner) { alarmList -> initRecyclerView(alarmList) }

        viewModel.setAlarmOff.observe(viewLifecycleOwner) {
            setAlarm(false, it)
        }

        // '+' 버튼 클릭 시 알람을 새로 추가할 수 있는 AddAlarmActivity 로 전환
        binding.btnAlarmAdd.setOnClickListener {
            val intent = Intent(activity, AddAlarmActivity::class.java)
            startActivity(intent)
        }

        checkNotificationPermission()

        return root
    }

    override fun onResume() {
        super.onResume()
        // 알람이 추가되거나 변경되고 나서 다시 이 activity 로 전환되었을 시 알람 목록 update
        viewModel.loadAlarms()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Recyclerview item Context Menu 에서 Item 선택 시 알람 해제 후 삭제
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            121 -> {
                val position = item.groupId
                viewModel.deleteAlarmAt(position)
                Toast.makeText(requireContext(), getString(R.string.delete_alarm), Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun initRecyclerView(alarmList: List<Alarm>) {
        val alarmRecyclerViewAdapter = AlarmAdapter()

        alarmRecyclerViewAdapter.listener = (object : AlarmAdapter.AdapterListener {
            // recyclerview 에서 Item 클릭 시 알람 정보를 변경할 수 있는 화면 전환
            override fun onItemClick(alarm: Alarm) {
                setAlarm(false, alarm)
                startActivity(Intent(activity, AddAlarmActivity::class.java).apply {
                    putExtra(Constants.ALARM_HOUR, alarm.alarmHour)
                    putExtra(Constants.ALARM_MINUTE, alarm.alarmMinute)
                    putExtra(Constants.ALARM_ID, alarm.id)
                })
            }

            override fun onCheckedChange(isChecked: Boolean, alarm: Alarm) {
                // 알람 toggle button 클릭 시 알람 설정/해제
                setAlarm(isChecked, alarm)
            }
        })

        // Adapter 에 있는 list 에 DB 에서 읽어온 list 반영
        alarmRecyclerViewAdapter.alarmList = alarmList.toCollection(ArrayList())

        binding.alarmRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = alarmRecyclerViewAdapter
        }

        // recyclerview empty view 설정
        if (alarmRecyclerViewAdapter.alarmList.isEmpty()) {
            binding.alarmRecyclerviewEmptyView.visibility = View.VISIBLE
        } else {
            binding.alarmRecyclerviewEmptyView.visibility = View.GONE
        }
    }

    // alarm switch 를 누르면 alarm 설정/해제
    private fun setAlarm(isChecked: Boolean, alarm: Alarm) {
        // DB update
        alarm.onOff = isChecked
        viewModel.updateAlarm(alarm)

        // isCheck == true 일 경우 알람 설정, isCheck == false 일 경우 알람 해제
        val alarmManager = requireContext().applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext().applicationContext, AlarmReceiver::class.java).apply {
            putExtra(Constants.ALARM_ID, alarm.id)
            putExtra(Constants.ALARM_HOUR, alarm.alarmHour)
            putExtra(Constants.ALARM_MINUTE, alarm.alarmMinute)
            putExtra(Constants.PLAYLIST_ID, alarm.playlistId)
            putExtra(Constants.PLAYLIST_TITLE, alarm.playlistName)
        }
        val pendingIntent = PendingIntent.getBroadcast(requireContext().applicationContext, alarm.id!!, intent, Constants.ALARM_FLAG)

        if (isChecked) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, alarm.alarmHour.toInt())
                set(Calendar.MINUTE, alarm.alarmMinute.toInt())

                if (before(Calendar.getInstance())) {
                    add(Calendar.DATE, Constants.CALENDAR_ADD_AMOUNT)
                }
            }

            // Special Permission 에 대한 권한 체크
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if ((requireContext().applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                        .canScheduleExactAlarms()
                ) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                    Toast.makeText(requireContext(), getString(R.string.complete_add_alarm), Toast.LENGTH_SHORT).show()
                } else {
                    startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                Toast.makeText(requireContext(), getString(R.string.complete_add_alarm), Toast.LENGTH_SHORT).show()
            }
        } else {
            // AlarmManager cancel
            alarmManager.cancel(pendingIntent)
            pendingIntent?.cancel()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext().applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.POST_NOTIFICATIONS)) {
                    // 이미 권한을 거절한 경우 권한 설정 화면으로 이동
                    Toast.makeText(context, getString(R.string.plz_permit_notification_permission), Toast.LENGTH_SHORT).show()
                    val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + requireActivity().packageName))
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    // 처음 권한 요청을 할 경우
                    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                        when (it) {
                            true -> {
                                Toast.makeText(context, getString(R.string.permit_notifiation_permission), Toast.LENGTH_SHORT).show()
                            }
                            false -> {
                                Toast.makeText(context, getString(R.string.request_notification_permission), Toast.LENGTH_SHORT).show()
                                requireActivity().moveTaskToBack(true)
                                requireActivity().finishAndRemoveTask()
                                exitProcess(0)
                            }
                        }
                    }.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}