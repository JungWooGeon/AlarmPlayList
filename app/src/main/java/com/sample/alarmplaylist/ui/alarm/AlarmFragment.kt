package com.sample.alarmplaylist.ui.alarm

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.room.Room
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.alarm_db.Alarm
import com.sample.alarmplaylist.alarm_db.AlarmDataBase
import com.sample.alarmplaylist.databinding.FragmentAlarmBinding

/**
 * https://github.com/arbelkilani/Clock-view
 */
class AlarmFragment : Fragment() {

    // @TODO
    // 1. 알람 기록할 때, 다른 여부도 저장
    // 2. 저장된 알람에서 activity 띄워서 정보 변경할 수 있도록 기능 추가
    // 3. 알람 기능 추가
    // 4. 알람음, 진동 설정 하기
    
    companion object {
        const val ALARM_DB = "alarmDB"
        const val INTENT_ALARM_HOUR = "alarmHour"
        const val INTENT_ALARM_MINUTE = "alarmMinute"
    }

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!
    private var ct: ViewGroup? = null
    private var list: List<Alarm>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ct = container

        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initClock()
        initButton()

        return root
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Recyclerview item 길게 누른 후 나오는 Context Menu 에서 Item 선택 시
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            // Item 삭제
            121 -> {
                val db : AlarmDataBase = Room.databaseBuilder(requireActivity().applicationContext, AlarmDataBase::class.java, ALARM_DB).build()
                val thread = Thread {
                    db.alarmDao().deleteAlarm(list!![item.groupId])
                }
                thread.start()
                thread.join()
                initRecyclerView()
            }
        }
        return true
    }

    private fun initClock() {
        binding.clock.setShowBorder(true)
        binding.clock.setHoursNeedleColor(R.color.red)
        binding.clock.setBorderColor(R.color.gainsboro)
    }

    private fun initRecyclerView() {
        val alarmRecyclerViewAdapter = AlarmAdapter()
        list = null

        // Item 클릭 시 알람 정보를 변경할 수 있는 화면으로 전환
        alarmRecyclerViewAdapter.listener = (object: AlarmAdapter.OnItemClickListener {
            override fun onItemClick(v: View, pos: Int) {
                val intent = Intent(ct!!.context, AddAlarmActivity::class.java)
                intent.putExtra(INTENT_ALARM_HOUR, list?.get(pos)?.alarmHour)
                intent.putExtra(INTENT_ALARM_MINUTE, list?.get(pos)?.alarmMinute)
                startActivity(intent)
            }
        })

        // DB 에서 데이터를 읽어온 후 adapter 에 반영
        val db : AlarmDataBase = Room.databaseBuilder(requireActivity().applicationContext, AlarmDataBase::class.java, ALARM_DB).build()
        val thread = Thread {
            list = db.alarmDao().getAll()

            list?.forEach {
                var result = if (it.alarmHour.toInt() >= 12) { "오후 " } else { "오전 " }
                result += if (it.alarmHour.toInt() == 0 || it.alarmHour.toInt() == 12) {
                    "12"
                } else if ((it.alarmHour.toInt() % 12) < 10) {
                    "0" + (it.alarmHour.toInt() % 12).toString()
                } else {
                    (it.alarmHour.toInt() % 12).toString()
                }
                result += if (it.alarmMinute.toInt() < 10) {
                    ":" + "0" + it.alarmMinute
                } else {
                    ":" + it.alarmMinute
                }

                alarmRecyclerViewAdapter.list.add(result)
            }
        }

        thread.start()
        thread.join()

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