package com.sample.alarmplaylist.alarm.dialog

import android.content.Context
import androidx.room.Room
import com.sample.alarmplaylist.Constant
import com.sample.alarmplaylist.playlist.playlist_db.PlayList
import com.sample.alarmplaylist.playlist.playlist_db.PlayListDataBase

/**
 * SelectPlaylistViewModel Model
 * 1. readAlarmData : DB 에서 플레이리스트 정보 조회
 */
class SelectPlaylistModel {
    var playList: List<PlayList>? = null

    fun readAlarmData(context: Context) {
        val db : PlayListDataBase = Room.databaseBuilder(context, PlayListDataBase::class.java, Constant.PLAYLIST_DB).build()

        val thread = Thread { playList = db.playListDao().getAll() }

        thread.start()
        thread.join()
    }
}