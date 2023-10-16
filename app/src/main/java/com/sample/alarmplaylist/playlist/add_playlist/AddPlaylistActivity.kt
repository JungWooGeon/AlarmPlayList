package com.sample.alarmplaylist.playlist.add_playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.databinding.ActivityAddPlaylistBinding
import com.sample.alarmplaylist.playlist.adapter.SearchAdapter
import com.sample.alarmplaylist.playlist.youtube_db.Youtube

/**
 * PlaylistFragment 에서 실행
 * 1. youtube 목록을 검색하고, 재생하는 기능
 * 2. 검색 후 영상 제목 옆 '+' 버튼을 통해 플레이리스트 목록에 추가하는 기능
 */
class AddPlaylistActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_PLAY_LIST_ID = -1
    }

    private lateinit var binding: ActivityAddPlaylistBinding
    private lateinit var viewModel: AddPlaylistViewModel
    private var searchResultRecyclerViewAdapter: SearchAdapter? = null
    private var playlistID = DEFAULT_PLAY_LIST_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlistID = intent.getIntExtra(Constants.PLAYLIST_ID, DEFAULT_PLAY_LIST_ID)

        binding = ActivityAddPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AddPlaylistViewModel::class.java]
        // DB 에서 youtubeList 정보 변경 시 recyclerview update
        viewModel.youtubeList.observe(this) { list -> initRecyclerView(list) }
        initSearchView()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPause() {
        // Adapter 내에서 YoutubePlayerView release 를 위하여 list 를 비워준 후 새로고침
        searchResultRecyclerViewAdapter?.list?.clear()
        searchResultRecyclerViewAdapter?.notifyDataSetChanged()
        searchResultRecyclerViewAdapter = null
        super.onPause()
    }

    // youtube 검색 recyclerview init
    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView(list: ArrayList<Youtube>) {
        searchResultRecyclerViewAdapter = SearchAdapter(lifecycle)
        // AddPlaylistActivity 와 adapter 사이에 통신하기 위한 listener
        searchResultRecyclerViewAdapter?.listener = (object : SearchAdapter.AdapterListener {
            // 추가 버튼 클릭 시 DB 에 반영
            override fun onAddButtonClick(pos: Int) {
                viewModel.addMusicToPlaylist(applicationContext, pos, playlistID)
                Toast.makeText(this@AddPlaylistActivity, getString(R.string.add_playlist), Toast.LENGTH_SHORT).show()
            }
        })

        // DB 에 있는 youtube list 반영
        searchResultRecyclerViewAdapter!!.list = list

        binding.searchResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = searchResultRecyclerViewAdapter
        }
    }

    private fun initSearchView() {
        // init SearchView
        binding.search.isSubmitButtonEnabled = true
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // 검색을 실행했을 경우, retrofit 을 사용하여 데이터 검색 후 추출하여 저장
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchYoutube(this@AddPlaylistActivity, query.toString(), playlistID, getString(R.string.youtube_api_search))
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean { return true }
        })
    }
}