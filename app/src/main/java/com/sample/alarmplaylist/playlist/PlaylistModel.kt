package com.sample.alarmplaylist.playlist

import android.content.Context
import androidx.room.Room
import com.sample.alarmplaylist.Constant
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.playlist.playlist_db.PlayList
import com.sample.alarmplaylist.playlist.playlist_db.PlayListDataBase
import com.sample.alarmplaylist.playlist.youtube_db.Youtube
import com.sample.alarmplaylist.playlist.youtube_db.YoutubeDataBase

/**
 * PlaylistViewModel Model
 * playList : 플레이리스트 정보 저장
 * musicList : 현재 선택되어 있는 플레이리스트에 관련된 음악 목록 저장
 * 1. readPlayList : DB 에서 플레이리스트 정보 조회
 * 2. addPlayList : DB 에서 플레이리스트 추가
 * 3. renamePlayList : DB 에서 선택된 플레이리스트 이름 변경
 * 4. deletePlayList : DB 에서 선택된 플레이리스트 삭제
 * 5. selectImg : 선택한 position 에 있는 플레이리스트와 관련된 음악 목록 조회
 * 6. deleteMusic : DB 에서 선택된 음악 삭제
 */
class PlaylistModel {

    companion object {
        private const val DEFAULT_SELECT_PLAYLIST_ID = 0
    }

    lateinit var playList : List<PlayList>
    lateinit var musicList : List<Youtube>

    var selectPlaylistID = DEFAULT_SELECT_PLAYLIST_ID

    fun readPlayList(context: Context) {
        val db : PlayListDataBase = Room.databaseBuilder(context, PlayListDataBase::class.java, Constant.PLAYLIST_DB).build()

        val thread = Thread { playList = db.playListDao().getAll() }

        thread.start()
        thread.join()
    }

    fun addPlayList(context: Context) {
        val db : PlayListDataBase = Room.databaseBuilder(context, PlayListDataBase::class.java, Constant.PLAYLIST_DB).build()

        val thread = Thread { db.playListDao().insertPlayList(PlayList(null, context.getString(R.string.playlist))) }

        thread.start()
        thread.join()
    }

    fun renamePlayList(context: Context, pos: Int, title: String) {
        playList[pos].playListTitle = title
        val db : PlayListDataBase = Room.databaseBuilder(context, PlayListDataBase::class.java, Constant.PLAYLIST_DB).build()

        val thread = Thread { db.playListDao().updatePlayList(PlayList(playList[pos].id, title)) }

        thread.start()
        thread.join()
    }

    fun deletePlayList(context: Context, pos: Int) {
        val playListDB : PlayListDataBase = Room.databaseBuilder(context, PlayListDataBase::class.java, Constant.PLAYLIST_DB).build()
        val youtubeDB : YoutubeDataBase = Room.databaseBuilder(context, YoutubeDataBase::class.java, Constant.YOUTUBE_DB).build()

        val thread = Thread {
            playListDB.playListDao().deletePlayList(playList[pos])
            youtubeDB.youtubeDao().deleteSelected(playList[pos].id!!)
        }

        thread.start()
        thread.join()

        selectPlaylistID = DEFAULT_SELECT_PLAYLIST_ID
    }

    fun selectImg(context: Context, pos: Int) {
        if (playList.isEmpty()) {
            musicList = ArrayList()
            return
        }

        selectPlaylistID = playList[pos].id!!

        val db : YoutubeDataBase = Room.databaseBuilder(context, YoutubeDataBase::class.java, Constant.YOUTUBE_DB).build()

        val thread = Thread { musicList = db.youtubeDao().getSelected(playList[pos].id!!) }

        thread.start()
        thread.join()
    }

    fun deleteMusic(context: Context, pos: Int) {
        val db : YoutubeDataBase = Room.databaseBuilder(context, YoutubeDataBase::class.java, Constant.YOUTUBE_DB).build()

        val thread = Thread { db.youtubeDao().delete(musicList[pos]) }

        thread.start()
        thread.join()

        val tmpList = ArrayList<Youtube>(musicList)
        tmpList.removeAt(pos)
        musicList = tmpList
    }
}