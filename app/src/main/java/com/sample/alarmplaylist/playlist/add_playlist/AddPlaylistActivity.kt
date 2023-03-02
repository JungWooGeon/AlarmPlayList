package com.sample.alarmplaylist.playlist.add_playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.sample.alarmplaylist.databinding.ActivityAddPlaylistBinding
import com.sample.alarmplaylist.playlist.adapter.SearchAdapter
import com.sample.alarmplaylist.playlist.api_retrofit.SearchVideoInterface
import com.sample.alarmplaylist.playlist.api_retrofit.SearchVideoResult
import com.sample.alarmplaylist.playlist.youtube_db.Youtube
import com.sample.alarmplaylist.playlist.youtube_db.YoutubeDataBase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddPlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPlaylistBinding
    private val searchURL = "https://www.googleapis.com/youtube/v3/"
    private lateinit var searchResultRecyclerViewAdapter: SearchAdapter
    private val API_KEY = ""
    private var playlistID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playlistID = intent.getIntExtra("playlistID", -1)

        initButton()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        searchResultRecyclerViewAdapter = SearchAdapter(lifecycle)
        searchResultRecyclerViewAdapter.listener = (object : SearchAdapter.AdapterListener {
            override fun onAddButtonClick(pos: Int) {
                val db : YoutubeDataBase = Room.databaseBuilder(
                    applicationContext,
                    YoutubeDataBase::class.java,
                    "YoutubeDB"
                ).build()

                val thread = Thread {
                    db.youtubeDao().insert(
                        Youtube(
                            null,
                            searchResultRecyclerViewAdapter.list[pos].videoId,
                            searchResultRecyclerViewAdapter.list[pos].title,
                            searchResultRecyclerViewAdapter.list[pos].thumbnail,
                            playlistID
                        )
                    )
                }

                thread.start()
                thread.join()

                Toast.makeText(this@AddPlaylistActivity, "플레이리스트에 추가되었습니다", Toast.LENGTH_SHORT).show()
            }
        })

        binding.searchResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = searchResultRecyclerViewAdapter
        }
    }

    private fun initButton() {
        binding.search.isSubmitButtonEnabled = true
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val retrofit = Retrofit.Builder().baseUrl(searchURL)
                    .addConverterFactory(GsonConverterFactory.create()).build()
                val service = retrofit.create(SearchVideoInterface::class.java)

                service.getSearchResult(
                    "snippet", "video", API_KEY,
                    query.toString()
                ).enqueue(object : Callback<SearchVideoResult> {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(call: Call<SearchVideoResult>, response: Response<SearchVideoResult>) {
                        if (response.isSuccessful) {
                            initRecyclerView()

                            for (i in response.body()?.items!!.indices) {
                                searchResultRecyclerViewAdapter.list.add(Youtube(
                                    null,
                                    response.body()?.items!![i].id.videoId,
                                    response.body()?.items!![i].snippet.title,
                                    response.body()?.items!![i].snippet.thumbnails.default.url,
                                    playlistID)
                                )
                            }

                            searchResultRecyclerViewAdapter.notifyDataSetChanged()

                            Log.d("테스트", searchResultRecyclerViewAdapter.list[0].title)
                        } else {
                            Log.d("테스트", "실패")
                        }
                    }

                    override fun onFailure(call: Call<SearchVideoResult>, t: Throwable) {
                        Log.d("테스트", "API Response 실패")
                    }
                })
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean { return true }
        })
    }
}