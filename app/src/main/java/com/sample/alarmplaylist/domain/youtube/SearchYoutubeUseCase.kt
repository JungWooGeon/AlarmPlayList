package com.sample.alarmplaylist.domain.youtube

import com.sample.alarmplaylist.data.entity.Youtube
import com.sample.alarmplaylist.data.repository.youtube.YoutubeRepository

class SearchYoutubeUseCase(private val repository: YoutubeRepository) {
    suspend operator fun invoke(query: String, playlistId: Int): List<Youtube> {
        return repository.searchYoutube(query, playlistId)
    }
}