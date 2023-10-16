package com.sample.alarmplaylist.presentation.select_playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.domain.playlist.GetAllPlaylistsUseCase
import com.sample.alarmplaylist.playlist.playlist_db.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SelectPlaylistViewModel(
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase
): ViewModel() {

    val playlist = MutableLiveData<List<Playlist>>()
    val saveAlarm = MutableLiveData<List<Playlist>>()

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            val allPlaylist = getAllPlaylistsUseCase()
            playlist.postValue(allPlaylist)
        }
    }

    fun saveAlarm() {
        saveAlarm.value = playlist.value
    }
}