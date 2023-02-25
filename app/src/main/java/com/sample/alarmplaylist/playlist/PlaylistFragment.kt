package com.sample.alarmplaylist.playlist

import android.content.*
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.alarm.receiver.AlarmReceiver
import com.sample.alarmplaylist.alarm.receiver.AlarmService
import com.sample.alarmplaylist.databinding.FragmentPlaylistBinding

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var ct: ViewGroup? = null

    private lateinit var prefs: SharedPreferences
    private lateinit var serviceIntent: Intent
    private var onPlay = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ct = container
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        prefs = ct?.context?.getSharedPreferences("playAlarmId", Context.MODE_PRIVATE)!!
        serviceIntent = Intent(requireActivity(), AlarmService::class.java)

        onPlay = prefs.getBoolean("onPlay", false)

        // 처음 view 셋팅
        if (onPlay) { binding.playStart.setImageResource(R.drawable.ic_pause) }

        // btn 셋팅
        binding.playStart.setOnClickListener {
            if (onPlay) {
                stopMusic()
            } else {
                startMusic()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun stopMusic() {
        // 종료
        requireActivity().stopService(serviceIntent)
        prefs.edit().putBoolean("onPlay", false).apply()
        binding.playStart.setImageResource(R.drawable.ic_play_arrow)
    }

    fun startMusic() {
        // 실행
        serviceIntent = Intent(requireActivity(), AlarmService::class.java)
        serviceIntent.putExtra("id", prefs.getString("id", "-1")?.toInt())
        serviceIntent.putExtra(AlarmReceiver.ALARM_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(serviceIntent)
        } else{
            requireActivity().startService(serviceIntent)
        }

        prefs.edit().putBoolean("onPlay", true).apply()
        binding.playStart.setImageResource(R.drawable.ic_pause)
    }
}