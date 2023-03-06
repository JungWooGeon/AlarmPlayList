package com.sample.alarmplaylist.playlist.play_youtube

import android.content.Context
import androidx.room.Room
import com.sample.alarmplaylist.Constant
import com.sample.alarmplaylist.playlist.youtube_db.Youtube
import com.sample.alarmplaylist.playlist.youtube_db.YoutubeDataBase

/**
 * YoutubePlayViewModel Model
 * 1. readYoutubeList : DB 에서 알람 설정 playlist ID 와 관련된 music list 조회
 */
class YoutubePlayModel {
    var youtubeList : List<Youtube>? = null

    fun readYoutubeList(context: Context, playListId: Int) {
        val db : YoutubeDataBase = Room.databaseBuilder(context, YoutubeDataBase::class.java, Constant.YOUTUBE_DB).build()

        val thread = Thread { youtubeList = db.youtubeDao().getSelected(playListId) }

        thread.start()
        thread.join()
    }
}