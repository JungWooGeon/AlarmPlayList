package com.sample.alarmplaylist.presentation.play_youtube

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.alarmplaylist.data.entity.Youtube
import com.sample.alarmplaylist.domain.youtube.GetSelectedYoutubesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class YoutubePlayViewModel(
    private val getSelectedYoutubesUseCase: GetSelectedYoutubesUseCase
): ViewModel() {

    val youtubes = MutableLiveData<List<Youtube>>()

    fun getSelectedYoutubesById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedYoutubes = getSelectedYoutubesUseCase(id)
            youtubes.postValue(selectedYoutubes)
        }
    }
}