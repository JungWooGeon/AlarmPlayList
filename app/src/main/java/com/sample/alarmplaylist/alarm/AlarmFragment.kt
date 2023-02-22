package com.sample.alarmplaylist.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.alarm.adapter.AlarmAdapter
import com.sample.alarmplaylist.alarm.add_alarm.AddAlarmActivity
import com.sample.alarmplaylist.alarm.receiver.AlarmReceiver
import com.sample.alarmplaylist.databinding.FragmentAlarmBinding
import java.util.*

/**
 * https://github.com/arbelkilani/Clock-view
 */
class AlarmFragment : Fragment() {

    // @TODO
    // 1. 알람 기록할 때, 다른 여부도 저장
    // 2. 알람 기능 추가 (On Off 설정은 완료)
    // 3. 알람음, 진동 설정 하기

    companion object {
        const val ALARM_DB = "alarmDB"
        const val INTENT_ALARM_HOUR = "alarmHour"
        const val INTENT_ALARM_MINUTE = "alarmMinute"
        const val INTENT_ALARM_ID = "alarmId"
        const val INTENT_ALARM_ON_OFF = "alarmOnOff"
        val ALARM_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
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
        when (item.itemId) {
            121 -> {
                viewModel.deleteAlarm(requireActivity().applicationContext, item.groupId)
            }
        }
        return true
    }

    private fun initClock() {
        binding.clock.setShowBorder(true)
        binding.clock.setHoursNeedleColor(R.color.red)
        binding.clock.setBorderColor(R.color.gainsboro)
    }

    private fun initRecyclerView(list: List<String>) {
        val alarmRecyclerViewAdapter = AlarmAdapter()

        alarmRecyclerViewAdapter.listener = (object : AlarmAdapter.AdapterListener {
            override fun onItemClick(v: View, pos: Int) {
                // Item 클릭 시 알람 정보를 변경할 수 있는 화면으로 전환
                val intent = Intent(ct!!.context, AddAlarmActivity::class.java)
                intent.putExtra(INTENT_ALARM_HOUR, viewModel.getAlarmInfo()?.get(pos)?.alarmHour)
                intent.putExtra(
                    INTENT_ALARM_MINUTE,
                    viewModel.getAlarmInfo()?.get(pos)?.alarmMinute
                )
                intent.putExtra(INTENT_ALARM_ID, viewModel.getAlarmInfo()?.get(pos)?.id)
                intent.putExtra(INTENT_ALARM_ON_OFF, viewModel.getOnOffList()[pos])
                startActivity(intent)
            }

            @SuppressLint("UnspecifiedImmutableFlag")
            override fun onCheckedChange(pos: Int, isChecked: Boolean) {
                // alarm switch 를 누르면 alarm 실행 혹은 취소
                viewModel.setCheckedChange(requireActivity().applicationContext, pos, isChecked)

                if (isChecked) {
                    val calendar = Calendar.getInstance().apply {
                        set(
                            Calendar.HOUR_OF_DAY,
                            viewModel.getAlarmInfo()?.get(pos)?.alarmHour!!.toInt()
                        )
                        set(
                            Calendar.MINUTE,
                            viewModel.getAlarmInfo()?.get(pos)?.alarmMinute!!.toInt()
                        )

                        if (before(Calendar.getInstance())) {
                            add(Calendar.DATE, 1)
                        }
                    }

                    val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val pendingIntent = PendingIntent.getBroadcast(
                        requireActivity(),
                        viewModel.getAlarmInfo()?.get(pos)?.id!!,
                        Intent(requireActivity(), AlarmReceiver::class.java),
                        ALARM_FLAG
                    )

                    // DOZE MODE 시에도 사용하려면
                    // alarmManager.setAndAllowWhileIdle()
                    // alarmManager.setExactAndAllowWhileIdle()

                    alarmManager.setExact(
                        // REALTIME_WAKE_UP 과의 차이 : REALTIME 을 더 권장 -> 휴대폰이 부팅되는 시점부터? 만약 서울에 있는데 휴대폰은 미국 시간으로 되어 있다면?
                        // 여기서는 Calendar 를 사용했기 때문에 RTC_WAKEUP
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    val pendingIntent = PendingIntent.getBroadcast(
                        requireActivity(),
                        viewModel.getAlarmInfo()?.get(pos)?.id!!,
                        Intent(requireActivity(), AlarmReceiver::class.java),
                        ALARM_FLAG
                    )
                    pendingIntent?.cancel()
                }
            }
        })

        // Adapter 에 있는 list 에 DB 에서 읽어온 list 반영
        list.forEach { alarmRecyclerViewAdapter.list.add(it) }
        viewModel.getOnOffList().forEach { alarmRecyclerViewAdapter.onOff.add(it) }

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