package com.sample.alarmplaylist.playlist.add_playlist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.alarmplaylist.data.entity.Youtube

/**
 * AddPlaylistActivity ViewModel
 * 1. addMusicToPlaylist : DB 에서 현재 플레이리스트에 음악 추가 요청
 * 2. searchYoutube : retrofit 사용, API 요청하여 검색 요청
 */
class AddPlaylistViewModel: ViewModel() {

    private val model = AddPlaylistModel()
    val youtubeList = MutableLiveData<ArrayList<Youtube>>()

    fun addMusicToPlaylist(context: Context, pos: Int, playlistID: Int) {
        model.addMusicToPlaylist(context, pos, playlistID)
    }

    fun searchYoutube(context: Context, query: String, playlistID: Int, youtubeApiSearch: String) {
        model.searchYoutube(context, query, playlistID, youtubeApiSearch)
        youtubeList.value = model.youtubeList
    }
}