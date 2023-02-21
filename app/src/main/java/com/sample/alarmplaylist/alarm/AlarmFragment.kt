package com.sample.alarmplaylist.alarm

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.alarm.adapter.AlarmAdapter
import com.sample.alarmplaylist.alarm.add_alarm.AddAlarmActivity
import com.sample.alarmplaylist.databinding.FragmentAlarmBinding

/**
 * https://github.com/arbelkilani/Clock-view
 */
class AlarmFragment : Fragment() {

    // @TODO
    // 1. 알람 기록할 때, 다른 여부도 저장
    // 2. 알람 기능 추가
    // 3. 알람음, 진동 설정 하기
    
    companion object {
        const val ALARM_DB = "alarmDB"
        const val INTENT_ALARM_HOUR = "alarmHour"
        const val INTENT_ALARM_MINUTE = "alarmMinute"
        const val INTENT_ALARM_ID = "alarmId"
    }

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!
    private var ct: ViewGroup? = null
    private lateinit var viewModel: AlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ct = container
        viewModel = ViewModelProvider(this)[AlarmViewModel::class.java]

        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.alarmList.observe(viewLifecycleOwner) { list -> initRecyclerView(list) }

        initClock()
        initButton()

        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel.readAlarmData(requireActivity().applicationContext)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Recyclerview item 길게 누른 후 나오는 Context Menu 에서 Item 선택 시 // Item 삭제
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId) { 121 -> { viewModel.deleteAlarm(requireActivity().applicationContext, item.groupId) } }
        return true
    }

    private fun initClock() {
        binding.clock.setShowBorder(true)
        binding.clock.setHoursNeedleColor(R.color.red)
        binding.clock.setBorderColor(R.color.gainsboro)
    }

    private fun initRecyclerView(list: List<String>) {
        val alarmRecyclerViewAdapter = AlarmAdapter()

        // Item 클릭 시 알람 정보를 변경할 수 있는 화면으로 전환
        alarmRecyclerViewAdapter.listener = (object: AlarmAdapter.OnItemClickListener {
            override fun onItemClick(v: View, pos: Int) {
                val intent = Intent(ct!!.context, AddAlarmActivity::class.java)
                intent.putExtra(INTENT_ALARM_HOUR, viewModel.getAlarmInfo()?.get(pos)?.alarmHour)
                intent.putExtra(INTENT_ALARM_MINUTE, viewModel.getAlarmInfo()?.get(pos)?.alarmMinute)
                intent.putExtra(INTENT_ALARM_ID, viewModel.getAlarmInfo()?.get(pos)?.id)
                startActivity(intent)
            }
        })

        // Adapter 에 있는 list 에 DB 에서 읽어온 list 반영
        list.forEach { alarmRecyclerViewAdapter.list.add(it) }

        binding.alarmRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = alarmRecyclerViewAdapter
        }
    }

    private fun initButton() {
        binding.btnAlarmAdd.setOnClickListener {
            val intent = Intent(ct!!.context, AddAlarmActivity::class.java)
            startActivity(intent)
        }
    }
}