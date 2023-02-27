package com.sample.alarmplaylist.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.alarm.adapter.AlarmAdapter
import com.sample.alarmplaylist.alarm.add_alarm.AddAlarmActivity
import com.sample.alarmplaylist.alarm.receiver.AlarmReceiver
import com.sample.alarmplaylist.alarm.receiver.AlarmService
import com.sample.alarmplaylist.databinding.FragmentAlarmBinding
import java.util.*

/**
 * https://github.com/arbelkilani/Clock-view
 */
class AlarmFragment : Fragment() {

    // @TODO
    // 2. 알람 저장 시 알람 여부 on 으로 설정하고 실제 알람 셋팅하기 o
    // 3. 알람 울리고 나면 알람 여부 off 로 설정하기 o
    // 4. 알람 기록할 때, 다른 여부도 저장
    // 5. 알람음 설정 기능 (알람음 설정 화면 포함)
    // 6. 진동 기능 구현하기
    // 7. 이미 실행되고 있는 미디어가 있으면 알람 or 음악 x
    // 8. youtube 설정 및 playlist 화면 구현

    // 플레이리스트 : 기본 알람음은 기본 플레이리스트로 만들어져 있음

    // 알람 참고해보기 : http://batmask.net/index.php/2021/03/12/786/
    //                 https://hanyeop.tistory.com/192
    // 알람 음악 플레이어 :  https://jizard.tistory.com/217
    //                    https://anhana.tistory.com/17

    // 테스트 순서 : 서비스 시작 버튼 (or 알람 설정) -> 노티 -> 노티 클릭 -> 메인 액티비티 실행
    //             -> 음악 종료
    //              플레이리스트 -> 플레이리스트 추가('''에서 이름 변경) -> 플레이리스트에서 음악 추가 (검색 액티비티)
    //             -> 알람 설정 시 저장해놓았던 플레이리스트를 알람음으로 설정
    //             -> 알람 시 기본 알람음으로 설정되어있으면 foreground service,
    //                플레이리스트로 설정되어있으면 youtube player activity 실행하여 플레이리스트 차례대로 실행
    //                (youtube player activity 는 종료 시 음악이 종료됨)
    
    // 추가 구현 : 저장되어 있는 음악들 중 선택하여 플레이리스트에 추가 (youtube 와 같이는 불가)

    companion object {
        const val ALARM_DB = "alarmDB"
        const val INTENT_ALARM_HOUR = "alarmHour"
        const val INTENT_ALARM_MINUTE = "alarmMinute"
        const val INTENT_ALARM_ID = "alarmId"
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

    private lateinit var serviceIntent: Intent

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

        serviceIntent = Intent(requireActivity(), AlarmService::class.java)
        if (requireActivity().intent.getBooleanExtra("off", false)) {
            requireActivity().stopService(serviceIntent)
            viewModel.setAlarmOff(requireActivity(),
                requireActivity().intent.getIntExtra("id", -1),
                requireActivity().intent.getStringExtra("hour").toString(),
                requireActivity().intent.getStringExtra("minute").toString()
            )
        }

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
                startActivity(Intent(ct!!.context, AddAlarmActivity::class.java).apply {
                    putExtra(INTENT_ALARM_HOUR, viewModel.getAlarmInfo()?.get(pos)?.alarmHour)
                    putExtra(INTENT_ALARM_MINUTE, viewModel.getAlarmInfo()?.get(pos)?.alarmMinute)
                    putExtra(INTENT_ALARM_ID, viewModel.getAlarmInfo()?.get(pos)?.id)
                })
            }

            override fun onCheckedChange(pos: Int, isChecked: Boolean) {
                setAlarm(pos, isChecked)
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

    private fun setAlarm(pos: Int, isChecked: Boolean) {
        // alarm switch 를 누르면 alarm 실행 혹은 취소
        viewModel.setCheckedChange(requireActivity().applicationContext, pos, isChecked)

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity(), AlarmReceiver::class.java).apply {
            putExtra("id", viewModel.getAlarmInfo()?.get(pos)?.id)
            putExtra("hour", viewModel.getAlarmInfo()?.get(pos)?.alarmHour)
            putExtra("minute", viewModel.getAlarmInfo()?.get(pos)?.alarmMinute)
            putExtra("알람음", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        }
        val pendingIntent = PendingIntent.getBroadcast(requireActivity(), viewModel.getAlarmInfo()?.get(pos)?.id!!, intent, ALARM_FLAG)

        if (isChecked) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, viewModel.getAlarmInfo()?.get(pos)?.alarmHour!!.toInt())
                set(Calendar.MINUTE, viewModel.getAlarmInfo()?.get(pos)?.alarmMinute!!.toInt())

                if (before(Calendar.getInstance())) {
                    add(Calendar.DATE, 1)
                }
            }

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Toast.makeText(ct?.context, "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            pendingIntent?.cancel()
        }
    }
}