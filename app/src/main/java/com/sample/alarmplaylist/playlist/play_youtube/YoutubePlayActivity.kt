package com.sample.alarmplaylist.playlist.play_youtube

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.sample.alarmplaylist.Constant
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.databinding.ActivityYoutubePlayBinding
import com.sample.alarmplaylist.playlist.adapter.MusicListAdapter
import com.sample.alarmplaylist.playlist.youtube_db.Youtube

/**
 * 실질적인 알람이 실행되는 Activity
 * YoutubePlayerView 에서 설정해놓은 youtube 가 재생됨
 * 이전 영상, 다음 영상 실행, 알람 종료 기능
 * 현재 재생 목록을 보여주는 recyclerview
 */
class YoutubePlayActivity : AppCompatActivity() {

    companion object {
        private const val INTENT_PLAYLIST_ID_DEFAULT_VALUE = -1
        private const val FIRST_PLAY_POSITION = 1
        private const val POSITION_DEDUCTION = 2
        private const val DEFAULT_CURRENT_PLAY_LIST_POSITION = 0
    }

    private lateinit var binding: ActivityYoutubePlayBinding
    private lateinit var viewModel: YoutubePlayViewModel

    private var currentPlaylistPosition = DEFAULT_CURRENT_PLAY_LIST_POSITION
    private lateinit var musicListRecyclerViewAdapter: MusicListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[YoutubePlayViewModel::class.java]
        turnScreenOnAndKeyguardOff()

        binding = ActivityYoutubePlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.readYoutubeList(applicationContext, intent.getIntExtra(Constant.PLAYLIST_ID, INTENT_PLAYLIST_ID_DEFAULT_VALUE))
        viewModel.youtubeList.observe(this) { list -> initMusicList(list)}

        // youtubePlayerView 설정
        lifecycle.addObserver(binding.youtubePlayerView)
        initYoutubePlayerView()
        initButton()
    }

    override fun onDestroy() {
        // 현재 사용하고 있는 youtubePlayerView 는 종료 시 release 가 필요
        binding.youtubePlayerView.release()
        super.onDestroy()
    }

    // 알람이 울릴 때, 화면 켜기 (안드로이드 버전 별로 설정)
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

    // init musicList recyclerview
    private fun initMusicList(musicList: List<Youtube>) {
        musicListRecyclerViewAdapter = MusicListAdapter(this, menuInflater)
        // YoutubePlayActivity 와 통신하기 위한 listener
        musicListRecyclerViewAdapter.listener = (object : MusicListAdapter.AdapterListener {
            // 음악 삭제 버튼을 누를 경우 경고 메시지 출력
            override fun deleteMusic(pos: Int) {
                Toast.makeText(this@YoutubePlayActivity, getString(R.string.try_to_main_activity), Toast.LENGTH_SHORT).show()
            }
        })

        // DB 에서 읽어온 music list 를 adapter list 에 반영
        musicListRecyclerViewAdapter.list = musicList as ArrayList<Youtube>
        binding.musicList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = musicListRecyclerViewAdapter
        }
    }

    private fun initButton() {
        // 이전 영상 실행 이벤트
        binding.skipPrevious.setOnClickListener {
            binding.youtubePlayerView.getYouTubePlayerWhenReady(object: YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    if (currentPlaylistPosition > FIRST_PLAY_POSITION) {
                        currentPlaylistPosition -= POSITION_DEDUCTION
                        youTubePlayer.loadVideo(
                            musicListRecyclerViewAdapter.list[currentPlaylistPosition++].videoId,
                            Constant.YOUTUBE_PLAYER_VIEW_START_SECONDS
                        )
                    }
                }
            })
        }

        // 다음 영상 실행 이벤트
        binding.skipNext.setOnClickListener {
            binding.youtubePlayerView.getYouTubePlayerWhenReady(object: YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    if (currentPlaylistPosition < musicListRecyclerViewAdapter.list.size)
                        youTubePlayer.loadVideo(
                            musicListRecyclerViewAdapter.list[currentPlaylistPosition++].videoId,
                            Constant.YOUTUBE_PLAYER_VIEW_START_SECONDS
                        )
                }
            })
        }

        // 알람 종료 (멈춤 버튼) 실행 이벤트
        binding.stop.setOnClickListener {
            binding.youtubePlayerView.getYouTubePlayerWhenReady(object: YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    Toast.makeText(this@YoutubePlayActivity, getString(R.string.finish_alarm), Toast.LENGTH_SHORT).show()
                    youTubePlayer.pause()
                    finish()
                }
            })
        }
    }

    private fun initYoutubePlayerView() {
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            // 준비가 되었을 경우 자동 실행 (loadVideo : 자동 실행 o, 썸네일 x, cueVideo : 자동 실행 x, 썸네일 o)
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (currentPlaylistPosition < musicListRecyclerViewAdapter.list.size) {
                    youTubePlayer.loadVideo(
                        musicListRecyclerViewAdapter.list[currentPlaylistPosition++].videoId,
                        Constant.YOUTUBE_PLAYER_VIEW_START_SECONDS
                    )
                }
            }

            // 영상이 끝났을 경우, 다음 재생 목록에 있는 영상 실행
            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                if (state == PlayerConstants.PlayerState.ENDED && currentPlaylistPosition < musicListRecyclerViewAdapter.list.size) {
                    youTubePlayer.loadVideo(
                        musicListRecyclerViewAdapter.list[currentPlaylistPosition++].videoId,
                        Constant.YOUTUBE_PLAYER_VIEW_START_SECONDS
                    )
                }
            }
        })
    }
}