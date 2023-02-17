package com.sample.alarmplaylist.ui.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.databinding.FragmentAlarmBinding

/**
 * https://github.com/arbelkilani/Clock-view
 */
class AlarmFragment : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val alarmViewModel =
            ViewModelProvider(this).get(AlarmViewModel::class.java)

        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.clock.setShowBorder(true)
        binding.clock.setHoursNeedleColor(R.color.red)
        binding.clock.setBorderColor(R.color.gainsboro)

        // set recyclerview
        val alarmRecyclerViewAdapter = AlarmAdapter()
        alarmRecyclerViewAdapter.list.add("테스트용 1")
        binding.alarmRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            adapter = alarmRecyclerViewAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}