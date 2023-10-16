package com.sample.alarmplaylist.domain.youtube

import com.sample.alarmplaylist.data.repository.youtube.YoutubeRepository

class DeleteYoutubeUseCase(private val repository: YoutubeRepository) {
    suspend operator fun invoke(id: Int) {
        return repository.deleteYoutube(id)
    }
}