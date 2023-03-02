package com.sample.alarmplaylist.playlist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.alarmplaylist.playlist.playlist_db.PlayList

class PlaylistViewModel : ViewModel() {

    private val model: PlaylistModel = PlaylistModel()
    val playList = MutableLiveData<List<PlayList>>()

    fun readPlayList(context: Context) {
        model.readPlayList(context)
        playList.value = model.playList
    }

    fun addPlayList(context: Context) {
        model.addPlayList(context)
        readPlayList(context)
    }

    fun renamePlayList(context: Context, pos: Int, title: String) {
        model.renamePlayList(context, pos, title)
    }

    fun deletePlayList(context: Context, pos: Int) {
        model.deletePlayList(context, pos)
        readPlayList(context)
        // @TODO 이 플레이리스트 ID를 가진 모든 음악 데이터도 같이 삭제
    }
}