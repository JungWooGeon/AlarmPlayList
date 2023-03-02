package com.sample.alarmplaylist.playlist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.alarmplaylist.playlist.playlist_db.PlayList
import com.sample.alarmplaylist.playlist.youtube_db.Youtube

class PlaylistViewModel : ViewModel() {

    private val model: PlaylistModel = PlaylistModel()
    val playList = MutableLiveData<List<PlayList>>()
    val musicList = MutableLiveData<List<Youtube>>()

    fun readPlayList(context: Context) {
        model.readPlayList(context)
        playList.value = model.playList
        selectImg(context, 0)
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
    }

    fun selectImg(context: Context, pos: Int) {
        model.selectImg(context, pos)
        musicList.value = model.musicList
    }

    fun getSelectPlaylistID() : Int {
        return model.selectPlaylistID
    }

    fun deleteMusic(context: Context, pos: Int) {
        model.deleteMusic(context, pos)
        musicList.value = model.musicList
    }
}