package com.sample.alarmplaylist.presentation.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.alarmplaylist.data.entity.Playlist
import com.sample.alarmplaylist.data.entity.Youtube
import com.sample.alarmplaylist.domain.playlist.AddPlaylistUseCase
import com.sample.alarmplaylist.domain.playlist.DeletePlaylistUseCase
import com.sample.alarmplaylist.domain.playlist.GetAllPlaylistsUseCase
import com.sample.alarmplaylist.domain.playlist.UpdatePlaylistUseCase
import com.sample.alarmplaylist.domain.youtube.DeleteYoutubeUseCase
import com.sample.alarmplaylist.domain.youtube.GetSelectedYoutubesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    private val addPlaylistUseCase: AddPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val getSelectedYoutubesUseCase: GetSelectedYoutubesUseCase,
    private val deleteYoutubeUseCase: DeleteYoutubeUseCase
) : ViewModel() {

    val playLists = MutableLiveData<List<Playlist>>()
    val youtubes = MutableLiveData<List<Youtube>>()

    fun loadPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            val allPlaylists = getAllPlaylistsUseCase()
            playLists.postValue(allPlaylists)
            if (allPlaylists.isNotEmpty()) {
                allPlaylists[0].id?.let { getSelectedYoutubesById(it) }
            } else {
                getSelectedYoutubesById(-1)
            }
        }
    }

    fun addPlayList(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addPlaylistUseCase(Playlist(null, title))
            loadPlaylists()
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            updatePlaylistUseCase(playlist)
        }
    }

    fun deletePlayList(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePlaylistUseCase(id)
            loadPlaylists()
        }
    }

    fun getSelectedYoutubesById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedYoutubes = getSelectedYoutubesUseCase(id)
            youtubes.postValue(selectedYoutubes)
        }
    }

    fun deleteYoutubeById(id: Int, selectedPlaylistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteYoutubeUseCase(id)
            getSelectedYoutubesById(selectedPlaylistId)
        }
    }

    fun playlistIsEmpty(): Boolean {
        return playLists.value?.isEmpty() ?: true
    }
}