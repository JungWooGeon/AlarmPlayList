package com.sample.alarmplaylist.presentation.add_playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.alarmplaylist.data.entity.Youtube
import com.sample.alarmplaylist.domain.youtube.AddYoutubeUseCase
import com.sample.alarmplaylist.domain.youtube.SearchYoutubeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddPlaylistViewModel(
    private val addYoutubeUseCase: AddYoutubeUseCase,
    private val searchYoutubeUseCase: SearchYoutubeUseCase
) : ViewModel() {

    val youtubeList = MutableLiveData<ArrayList<Youtube>>()
    val errorEvent = MutableLiveData<Boolean>()

    fun addYoutube(youtube: Youtube) {
        viewModelScope.launch(Dispatchers.IO) {
            addYoutubeUseCase.invoke(youtube)
        }
    }

    fun searchYoutube(query: String, playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = searchYoutubeUseCase(query, playlistId)
                youtubeList.postValue(result.toCollection(ArrayList()))
            } catch (e: Exception) {
                // 네트워크 오류
                withContext(Dispatchers.Main) {
                    errorEvent.postValue(true)
                }
            }
        }
    }
}