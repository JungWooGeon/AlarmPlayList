package com.sample.alarmplaylist.ui.alarm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

    companion object {
        const val ALARM_DB = "alarmDB"
    }

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)

        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initClock()
        initRecyclerView()
        initButton(container!!)

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

    private fun initClock() {
        binding.clock.setShowBorder(true)
        binding.clock.setHoursNeedleColor(R.color.red)
        binding.clock.setBorderColor(R.color.gainsboro)
    }

    private fun initRecyclerView() {
        val alarmRecyclerViewAdapter = AlarmAdapter()
        val db : AlarmDataBase = Room.databaseBuilder(requireActivity().applicationContext, AlarmDataBase::class.java, ALARM_DB).build()
        val thread = Thread {
            val list : List<Alarm> = db.alarmDao().getAll()

            list.forEach {
                var result = if (it.alarmHour.toInt() >= 12) { "오후 " } else { "오전 " }
                result += (it.alarmHour.toInt() % 12).toString() + ":" + it.alarmMinute

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

    private fun initButton(container: ViewGroup) {
        binding.btnAlarmAdd.setOnClickListener {
            val intent = Intent(container.context, AddAlarmActivity::class.java)
            startActivity(intent)
        }
    }
}