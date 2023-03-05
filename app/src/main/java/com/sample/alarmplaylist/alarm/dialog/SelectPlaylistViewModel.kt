package com.sample.alarmplaylist.alarm.dialog

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.alarmplaylist.playlist.playlist_db.PlayList

/**
 * SelectPlaylistDialog ViewModel
 * 1. readAlarmData : DB 에서 플레이리스트 정보 조회 요청
 */
class SelectPlaylistViewModel: ViewModel() {

    private val model = SelectPlaylistModel()
    val playList = MutableLiveData<List<PlayList>>()

    fun readAlarmData(context: Context) {
        model.readAlarmData(context)
        playList.value = model.playList
    }
}