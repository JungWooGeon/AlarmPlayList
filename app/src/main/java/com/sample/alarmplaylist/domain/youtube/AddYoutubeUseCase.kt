package com.sample.alarmplaylist.domain.youtube

import com.sample.alarmplaylist.data.entity.Youtube
import com.sample.alarmplaylist.data.repository.youtube.YoutubeRepository

class AddYoutubeUseCase(private val repository: YoutubeRepository) {
    suspend operator fun invoke(youtube: Youtube) {
        return repository.addYoutube(youtube)
    }
}