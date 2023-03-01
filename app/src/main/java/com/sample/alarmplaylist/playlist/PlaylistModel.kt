package com.sample.alarmplaylist.playlist

import android.content.Context
import androidx.room.Room
import com.sample.alarmplaylist.playlist.playlist_db.PlayList
import com.sample.alarmplaylist.playlist.playlist_db.PlayListDataBase

class PlaylistModel {
    lateinit var playList : List<PlayList>

    fun readPlayList(context: Context) {
        val db : PlayListDataBase = Room.databaseBuilder(context, PlayListDataBase::class.java,
            "playListDB"
        ).build()

        val thread = Thread {
           playList = db.playListDao().getAll()
        }

        thread.start()
        thread.join()
    }

    fun addPlayList(context: Context) {
        val db : PlayListDataBase = Room.databaseBuilder(context, PlayListDataBase::class.java,
            "playListDB"
        ).build()

        val thread = Thread {
            db.playListDao().insertPlayList(PlayList(
                null, "플레이리스트"
            ))
        }

        thread.start()
        thread.join()
    }
}