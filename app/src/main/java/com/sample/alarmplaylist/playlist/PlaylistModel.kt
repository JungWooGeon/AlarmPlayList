package com.sample.alarmplaylist.playlist

import android.content.Context
import androidx.room.Room
import com.sample.alarmplaylist.playlist.playlist_db.PlayList
import com.sample.alarmplaylist.playlist.playlist_db.PlayListDataBase
import com.sample.alarmplaylist.playlist.youtube_db.Youtube
import com.sample.alarmplaylist.playlist.youtube_db.YoutubeDataBase

class PlaylistModel {

    lateinit var playList : List<PlayList>
    lateinit var musicList : List<Youtube>

    var selectPlaylistID = 0

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

    fun renamePlayList(context: Context, pos: Int, title: String) {
        playList[pos].playListTitle = title
        val db : PlayListDataBase = Room.databaseBuilder(context, PlayListDataBase::class.java,
            "playListDB"
        ).build()

        val thread = Thread {
            db.playListDao().updatePlayList(
                PlayList(
                playList[pos].id, title
            )
            )
        }

        thread.start()
        thread.join()
    }

    fun deletePlayList(context: Context, pos: Int) {
        val playListDB : PlayListDataBase = Room.databaseBuilder(context, PlayListDataBase::class.java,
            "playListDB"
        ).build()
        val youtubeDB : YoutubeDataBase = Room.databaseBuilder(context, YoutubeDataBase::class.java,
            "YoutubeDB"
        ).build()

        val thread = Thread {
            playListDB.playListDao().deletePlayList(playList[pos])
            youtubeDB.youtubeDao().deleteSelected(playList[pos].id!!)
        }

        thread.start()
        thread.join()

        selectPlaylistID = 0
    }

    fun selectImg(context: Context, pos: Int) {
        if (playList.isEmpty()) {
            musicList = ArrayList()
            return
        }

        selectPlaylistID = playList[pos].id!!

        val db : YoutubeDataBase = Room.databaseBuilder(context, YoutubeDataBase::class.java,
            "YoutubeDB"
        ).build()

        val thread = Thread {
            musicList = db.youtubeDao().getSelected(playList[pos].id!!)
        }

        thread.start()
        thread.join()
    }

    fun deleteMusic(context: Context, pos: Int) {
        val db : YoutubeDataBase = Room.databaseBuilder(context, YoutubeDataBase::class.java,
            "YoutubeDB"
        ).build()

        val thread = Thread {
            db.youtubeDao().delete(musicList[pos])
        }
        thread.start()
        thread.join()

        val tmpList = ArrayList<Youtube>(musicList)
        tmpList.removeAt(pos)
        musicList = tmpList
    }
}