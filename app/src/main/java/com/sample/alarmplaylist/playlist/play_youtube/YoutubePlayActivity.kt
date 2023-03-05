package com.sample.alarmplaylist.playlist.play_youtube

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.sample.alarmplaylist.databinding.ActivityYoutubePlayBinding
import com.sample.alarmplaylist.playlist.adapter.MusicListAdapter
import com.sample.alarmplaylist.playlist.youtube_db.Youtube
import com.sample.alarmplaylist.playlist.youtube_db.YoutubeDataBase

class YoutubePlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityYoutubePlayBinding

    private var currentPlaylistPosition = 0
    private lateinit var musicListRecyclerViewAdapter: MusicListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        turnScreenOnAndKeyguardOff()

        binding = ActivityYoutubePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readPlaylist()

        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (currentPlaylistPosition < musicListRecyclerViewAdapter.list.size) {
                    youTubePlayer.loadVideo(musicListRecyclerViewAdapter.list[currentPlaylistPosition++].videoId, 0F)
                }
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                if (state == PlayerConstants.PlayerState.ENDED && currentPlaylistPosition < musicListRecyclerViewAdapter.list.size) {
                    youTubePlayer.loadVideo(musicListRecyclerViewAdapter.list[currentPlaylistPosition++].videoId, 0F)
                }
            }
        })

        initButton()
    }

    override fun onDestroy() {
        binding.youtubePlayerView.release()
        super.onDestroy()
    }

    private fun readPlaylist() {
        val db : YoutubeDataBase = Room.databaseBuilder(this, YoutubeDataBase::class.java,
            "YoutubeDB"
        ).build()

        val thread = Thread {
            initMusicList(db.youtubeDao().getSelected(intent.getIntExtra("playlistId", -1)))
        }

        thread.start()
        thread.join()
    }

    private fun turnScreenOnAndKeyguardOff(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED    // deprecated api 27
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD     // deprecated api 26
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON   // deprecated api 27
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
        }
        val keyguardMgr = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyguardMgr.requestDismissKeyguard(this, null)
        }
    }

    private fun initMusicList(musicList: List<Youtube>) {
        musicListRecyclerViewAdapter = MusicListAdapter(this, menuInflater)
        musicListRecyclerViewAdapter.listener = (object : MusicListAdapter.AdapterListener {
            override fun deleteMusic(pos: Int) {
                Toast.makeText(this@YoutubePlayActivity, "메인 화면에서 삭제를 시도해주세요", Toast.LENGTH_SHORT).show()
            }
        })
        musicListRecyclerViewAdapter.list = musicList as ArrayList<Youtube>
        binding.musicList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = musicListRecyclerViewAdapter
        }
    }

    private fun initButton() {
        binding.skipPrevious.setOnClickListener {
            binding.youtubePlayerView.getYouTubePlayerWhenReady(object: YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    if (currentPlaylistPosition > 1) {
                        currentPlaylistPosition -= 2
                        youTubePlayer.loadVideo(musicListRecyclerViewAdapter.list[currentPlaylistPosition++].videoId, 0F)
                    }
                }
            })
        }

        binding.skipNext.setOnClickListener {
            binding.youtubePlayerView.getYouTubePlayerWhenReady(object: YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    if (currentPlaylistPosition < musicListRecyclerViewAdapter.list.size)
                        youTubePlayer.loadVideo(musicListRecyclerViewAdapter.list[currentPlaylistPosition++].videoId, 0F)
                }
            })
        }

        binding.stop.setOnClickListener {
            binding.youtubePlayerView.getYouTubePlayerWhenReady(object: YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    Toast.makeText(this@YoutubePlayActivity, "알람이 종료되었습니다", Toast.LENGTH_SHORT).show()
                    youTubePlayer.pause()
                    finish()
                }
            })
        }
    }
}