package com.sample.alarmplaylist.data.repository.youtube

import com.sample.alarmplaylist.data.db.youtube.YoutubeDataBase
import com.sample.alarmplaylist.data.entity.Youtube

class YoutubeRepositoryImpl(private val db: YoutubeDataBase): YoutubeRepository {

    override suspend fun getSelectedYoutubes(id: Int): List<Youtube> {
        return db.youtubeDao().getSelected(id)
    }

    override suspend fun addYoutube(youtube: Youtube) {
        return db.youtubeDao().insert(youtube)
    }

    override suspend fun deleteYoutube(id: Int) {
        return db.youtubeDao().deleteYoutubeById(id)
    }
}