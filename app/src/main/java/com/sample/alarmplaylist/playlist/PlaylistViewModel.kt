package com.sample.alarmplaylist.playlist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sample.alarmplaylist.playlist.playlist_db.Playlist
import com.sample.alarmplaylist.playlist.youtube_db.Youtube

/**
 * PlaylistFragment ViewModel
 * 1. readPlayList : DB 에서 플레이리스트 목록 조회 요청
 * 2. addPlayList : DB 에 플레이리스트 목록 추가 요청
 * 3. renamePlayList : DB 에 플레이리스트 이름 변경 요청
 * 4. deletePlayList : DB 에 플레이리스트 삭제 요청
 * 5. selectImg : 선택한 position 에 있는 플레이리스트에 대한 음악 목록 요청
 * 6. getSelectPlaylistID : 현재 선택되어 있는 플레이리스트 ID 조회
 * 7. deleteMusic : DB 에 음악 삭제 요청
 */
class PlaylistViewModel : ViewModel() {

    private val model: PlaylistModel = PlaylistModel()
    val playList = MutableLiveData<List<Playlist>>()
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

    fun playlistIsEmpty(): Boolean {
        return model.playList.isEmpty()
    }
}