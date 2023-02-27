package com.sample.alarmplaylist.playlist

import android.os.Bundle
import android.util.Log
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubeInitializationResult
import com.sample.alarmplaylist.databinding.ActivityAddPlaylistBinding

class AddPlaylistActivity : YouTubeBaseActivity() {

    private lateinit var binding: ActivityAddPlaylistBinding
    private val API_KEY = "AIzaSyCCNN5OovIZfuaCU4F0Mg8n_ttXvJ-LNAU"
    private val videoId = "BBdC1rl5sKY";

    private lateinit var youTubePlayer: YouTubePlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.youTubePlayerView.initialize(API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider, p1: YouTubePlayer, p2: Boolean) {
                youTubePlayer = p1
                youTubePlayer.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
                    override fun onLoading() {}

                    override fun onLoaded(p0: String?) { youTubePlayer.play() }

                    override fun onAdStarted() {}

                    override fun onVideoStarted() {}

                    override fun onVideoEnded() {}

                    override fun onError(p0: YouTubePlayer.ErrorReason?) {
                        Log.d("테스트", p0.toString())
                    }
                })
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                Log.d("테스트", p1.toString())
            }
        })

        binding.youtubeBtn.setOnClickListener {
            if (youTubePlayer.isPlaying) {
                youTubePlayer.pause()
            }
            youTubePlayer.cueVideo(videoId)
        }
    }
}