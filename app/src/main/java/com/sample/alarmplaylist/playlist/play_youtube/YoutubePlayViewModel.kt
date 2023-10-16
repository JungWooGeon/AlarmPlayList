package com.sample.alarmplaylist.playlist.play_youtube

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.alarmplaylist.data.entity.Youtube

/**
 * YoutubePlayActivity ViewModel
 * 1. readYoutubeList : DB 에서 음악 리스트 조회 요청
 */
class YoutubePlayViewModel: ViewModel() {

    private val model = YoutubePlayModel()
    val youtubeList = MutableLiveData<List<Youtube>>()

    fun readYoutubeList(context: Context, playListId: Int) {
        model.readYoutubeList(context, playListId)
        youtubeList.value = model.youtubeList
    }
}