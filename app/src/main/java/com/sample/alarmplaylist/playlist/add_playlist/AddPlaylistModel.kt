package com.sample.alarmplaylist.playlist.add_playlist

import android.content.Context
import androidx.room.Room
import com.sample.alarmplaylist.BuildConfig
import com.sample.alarmplaylist.Constant
import com.sample.alarmplaylist.playlist.api_retrofit.SearchVideoInterface
import com.sample.alarmplaylist.playlist.youtube_db.Youtube
import com.sample.alarmplaylist.playlist.youtube_db.YoutubeDataBase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  AddPlaylistViewModel Model
 *  1. addMusicToPlaylist : DB 에서 현재 플레이리스트에 음악 추가
 *  2. searchYoutube : retrofit 사용, API 요청하여 검색
 */
class AddPlaylistModel {

    companion object {
        private const val RETROFIT_SEARCH_PART = "snippet"
        private const val RETROFIT_SEARCH_TYPE = "video"
    }

    val youtubeList = ArrayList<Youtube>()

    fun addMusicToPlaylist(context: Context, pos: Int, playlistID: Int) {
        val db : YoutubeDataBase = Room.databaseBuilder(context, YoutubeDataBase::class.java, Constant.YOUTUBE_DB).build()

        val thread = Thread {
            db.youtubeDao().insert(Youtube(null, youtubeList[pos].videoId, youtubeList[pos].title, youtubeList[pos].thumbnail, playlistID))
        }

        thread.start()
        thread.join()
    }

    fun searchYoutube(query: String, playlistID: Int) {
        // retrofit 동기적 사용
        val retrofit = Retrofit.Builder().baseUrl(BuildConfig.YOUTUBE_API_SEARCH)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(SearchVideoInterface::class.java)

        val thread = Thread {
            val response = service.getSearchResult(
                RETROFIT_SEARCH_PART,
                RETROFIT_SEARCH_TYPE,
                BuildConfig.YOUTUBE_API_KEY,
                query
            ).execute().body()

            youtubeList.clear()

            for (i in response?.items!!.indices) {
                youtubeList.add(Youtube(null,
                    response.items[i].id.videoId,
                    response.items[i].snippet.title,
                    response.items[i].snippet.thumbnails.default.url,
                    playlistID
                ))
            }
        }

        thread.start()
        thread.join()
    }
}