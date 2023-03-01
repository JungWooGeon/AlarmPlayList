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
}