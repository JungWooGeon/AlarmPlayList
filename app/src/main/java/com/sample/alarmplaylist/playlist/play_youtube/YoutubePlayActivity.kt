package com.sample.alarmplaylist.playlist.play_youtube

import android.os.Bundle
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubePlayer
import com.sample.alarmplaylist.databinding.ActivityYoutubePlayBinding

class YoutubePlayActivity : YouTubeBaseActivity() {
    private lateinit var binding: ActivityYoutubePlayBinding

    private lateinit var youTubePlayer: YouTubePlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityYoutubePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}