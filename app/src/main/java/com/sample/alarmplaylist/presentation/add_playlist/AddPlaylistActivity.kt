package com.sample.alarmplaylist.presentation.add_playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.data.entity.Youtube
import com.sample.alarmplaylist.databinding.ActivityAddPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPlaylistActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_PLAY_LIST_ID = -1
    }

    private lateinit var binding: ActivityAddPlaylistBinding
    private val viewModel by viewModel<AddPlaylistViewModel>()

    private var searchResultRecyclerViewAdapter: SearchAdapter? = null
    private var playlistID = DEFAULT_PLAY_LIST_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlistID = intent.getIntExtra(Constants.PLAYLIST_ID, DEFAULT_PLAY_LIST_ID)

        binding = ActivityAddPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DB 에서 youtubeList 정보 변경 시 recyclerview update
        viewModel.youtubeList.observe(this) { list -> initRecyclerView(list) }

        // 네트워크 오류 발생 시 토스트 메시지 출력
        viewModel.errorEvent.observe(this) {
            Toast.makeText(this, getString(R.string.fail_search_retry), Toast.LENGTH_SHORT).show()
        }
        initSearchView()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDestroy() {
        // Adapter 내에서 YoutubePlayerView release 를 위하여 list 를 비워준 후 새로고침
        searchResultRecyclerViewAdapter?.list?.clear()
        searchResultRecyclerViewAdapter?.notifyDataSetChanged()
        searchResultRecyclerViewAdapter = null
        super.onDestroy()
    }

    // youtube 검색 recyclerview init
    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView(list: ArrayList<Youtube>) {
        searchResultRecyclerViewAdapter = SearchAdapter(lifecycle)
        // AddPlaylistActivity 와 adapter 사이에 통신하기 위한 listener
        searchResultRecyclerViewAdapter?.listener = (object : SearchAdapter.AdapterListener {
            // 추가 버튼 클릭 시 DB 에 반영
            override fun onAddButtonClick(youtube: Youtube) {
                viewModel.addYoutube(Youtube(null, youtube.videoId, youtube.title, youtube.thumbnail, playlistID))
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
        binding.search.isSubmitButtonEnabled = true
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // 검색을 실행했을 경우, retrofit 을 사용하여 데이터 검색 후 추출하여 저장
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchYoutube(query.toString(), playlistID)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean { return true }
        })
    }
}