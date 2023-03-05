package com.sample.alarmplaylist

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.sample.alarmplaylist.databinding.ActivityMainBinding

/**
 * 메인 화면을 구성하고, bottom navigation bar 에서 알람 fragment 와 플레이리스트 fragment 로 전환한다.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        AppBarConfiguration(
            setOf(
                R.id.navigation_alarm, R.id.navigation_playlist
            )
        )

        navView.setupWithNavController(navController)
    }
}