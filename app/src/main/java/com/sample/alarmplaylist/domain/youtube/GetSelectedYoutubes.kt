package com.sample.alarmplaylist.domain.youtube

import com.sample.alarmplaylist.data.entity.Youtube
import com.sample.alarmplaylist.data.repository.youtube.YoutubeRepository

class GetSelectedYoutubes(private val repository: YoutubeRepository) {
    suspend operator fun invoke(id: Int): List<Youtube> {
        return repository.getSelectedYoutubes(id)
    }
}