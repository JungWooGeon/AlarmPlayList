package com.sample.alarmplaylist.data.repository.youtube

import com.sample.alarmplaylist.data.entity.Youtube

interface YoutubeRepository {
    suspend fun getSelectedYoutubes(id: Int): List<Youtube>
    suspend fun addYoutube(youtube: Youtube)
    suspend fun deleteYoutube(id: Int)
}