package com.sample.alarmplaylist.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.alarmplaylist.Constant
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.alarm.adapter.AlarmAdapter
import com.sample.alarmplaylist.alarm.add_alarm.AddAlarmActivity
import com.sample.alarmplaylist.alarm.notification.AlarmReceiver
import com.sample.alarmplaylist.databinding.FragmentAlarmBinding
import java.util.*

/**
 * 알람 정보를 보여주는 화면이고, 알람을 추가하거나 변경할 수 있는 AddAlarmActivity 를 띄우는 역할을 한다.
 */
class AlarmFragment : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AlarmViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this)[AlarmViewModel::class.java]
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 알람 화면에 보여지는 알람 정보가 변경될 경우 update
        viewModel.alarmList.observe(viewLifecycleOwner) { list -> initRecyclerView(list) }

        // '+' 버튼 클릭 시 알람을 새로 추가할 수 있는 AddAlarmActivity 로 전환
        binding.btnAlarmAdd.setOnClickListener {
            val intent = Intent(activity, AddAlarmActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        // 알람이 추가되거나 변경되고 나서 다시 이 activity 로 전환되었을 시 알람 목록 update
        viewModel.readAlarmData(requireActivity().applicationContext)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Recyclerview item Context Menu 에서 Item 선택 시 알람 해제 후 삭제
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            121 -> {
                setAlarm(item.groupId, false)
                viewModel.deleteAlarm(requireActivity().applicationContext, item.groupId)
            }
        }
        return true
    }

    private fun initRecyclerView(list: List<String>) {
        val alarmRecyclerViewAdapter = AlarmAdapter()

        alarmRecyclerViewAdapter.listener = (object : AlarmAdapter.AdapterListener {
            // recyclerview 에서 Item 클릭 시 알람 정보를 변경할 수 있는 화면 전환
            override fun onItemClick(v: View, pos: Int) {
                setAlarm(pos, false)
                startActivity(Intent(activity, AddAlarmActivity::class.java).apply {
                    putExtra(Constant.ALARM_HOUR, viewModel.getAlarmInfo()?.get(pos)?.alarmHour)
                    putExtra(Constant.ALARM_MINUTE, viewModel.getAlarmInfo()?.get(pos)?.alarmMinute)
                    putExtra(Constant.ALARM_ID, viewModel.getAlarmInfo()?.get(pos)?.id)
                })
            }

            override fun onCheckedChange(pos: Int, isChecked: Boolean) {
                // 알람 toggle button 클릭 시 알람 설정/해제
                setAlarm(pos, isChecked)
            }
        })

        // Adapter 에 있는 list 에 DB 에서 읽어온 list 반영
        list.forEach { alarmRecyclerViewAdapter.list.add(it) }
        viewModel.getOnOffList().forEach { alarmRecyclerViewAdapter.onOff.add(it) }
        viewModel.getAlarmInfo()?.forEach { alarmRecyclerViewAdapter.playlistName.add(it.playlistName) }

        binding.alarmRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = alarmRecyclerViewAdapter
        }

        // recyclerview empty view 설정
        if (alarmRecyclerViewAdapter.list.isEmpty()) {
            binding.alarmRecyclerviewEmptyView.visibility = View.VISIBLE
        } else {
            binding.alarmRecyclerviewEmptyView.visibility = View.GONE
        }
    }

    // alarm switch 를 누르면 alarm 설정/해제
    private fun setAlarm(pos: Int, isChecked: Boolean) {
        // DB update
        viewModel.setCheckedChange(requireActivity().applicationContext, pos, isChecked)

        // isCheck == true 일 경우 알람 설정, isCheck == false 일 경우 알람 해제
        val alarmManager = requireActivity().applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity().applicationContext, AlarmReceiver::class.java).apply {
            putExtra(Constant.ALARM_ID, viewModel.getAlarmInfo()?.get(pos)?.id)
            putExtra(Constant.ALARM_HOUR, viewModel.getAlarmInfo()?.get(pos)?.alarmHour)
            putExtra(Constant.ALARM_MINUTE, viewModel.getAlarmInfo()?.get(pos)?.alarmMinute)
            putExtra(Constant.PLAYLIST_ID, viewModel.getAlarmInfo()?.get(pos)?.playlistId)
            putExtra(Constant.PLAYLIST_TITLE, viewModel.getAlarmInfo()?.get(pos)?.playlistName)
        }
        val pendingIntent = PendingIntent.getBroadcast(requireActivity().applicationContext, viewModel.getAlarmInfo()?.get(pos)?.id!!, intent, Constant.ALARM_FLAG)

        if (isChecked) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, viewModel.getAlarmInfo()?.get(pos)?.alarmHour!!.toInt())
                set(Calendar.MINUTE, viewModel.getAlarmInfo()?.get(pos)?.alarmMinute!!.toInt())

                if (before(Calendar.getInstance())) {
                    add(Calendar.DATE, Constant.CALENDAR_ADD_AMOUNT)
                }
            }

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Toast.makeText(activity, getString(R.string.complete_add_alarm), Toast.LENGTH_SHORT).show()
        } else {
            // AlarmManager cancel
            alarmManager.cancel(pendingIntent)
            pendingIntent?.cancel()
        }
    }
}