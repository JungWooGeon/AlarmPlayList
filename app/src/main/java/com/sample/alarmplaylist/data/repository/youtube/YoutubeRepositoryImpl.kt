package com.sample.alarmplaylist.data.repository.youtube

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.alarmplaylist.BuildConfig
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.data.db.youtube.YoutubeDataBase
import com.sample.alarmplaylist.data.entity.Youtube
import com.sample.alarmplaylist.data.network.SearchVideoResult
import com.sample.alarmplaylist.data.network.SearchYoutubeInterface
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YoutubeRepositoryImpl(
    private val db: YoutubeDataBase,
    private val service: SearchYoutubeInterface
) : YoutubeRepository {

    override suspend fun getSelectedYoutubes(id: Int): List<Youtube> {
        return db.youtubeDao().getSelected(id)
    }

    override suspend fun addYoutube(youtube: Youtube) {
        return db.youtubeDao().insert(youtube)
    }

    override suspend fun deleteYoutube(id: Int) {
        return db.youtubeDao().deleteYoutubeById(id)
    }

    override suspend fun searchYoutube(query: String, playlistId: Int): List<Youtube> = suspendCancellableCoroutine { continuation ->
        val call = service.getSearchResult(
            Constants.RETROFIT_SEARCH_PART,
            Constants.RETROFIT_SEARCH_TYPE,
            BuildConfig.YOUTUBE_API_KEY,
            query
        )

        call.enqueue(object : Callback<SearchVideoResult> {
            override fun onResponse(
                call: Call<SearchVideoResult>,
                response: Response<SearchVideoResult>
            ) {
                if (response.isSuccessful) {
                    val youtubeList = response.body()?.items?.map { item ->
                        Youtube(
                            null,
                            item.id.videoId,
                            item.snippet.title,
                            item.snippet.thumbnails.default.url,
                            playlistId
                        )
                    } ?: emptyList()

                    continuation.resumeWith(Result.success(youtubeList))
                } else {
                    continuation.resumeWith(Result.failure(RuntimeException("Response is not successful")))
                }
            }

            override fun onFailure(call: Call<SearchVideoResult>, t: Throwable) {
                continuation.resumeWith(Result.failure(t))
            }
        })

        // 코루틴이 종료될 경우, 네트워크 요청도 종료
        continuation.invokeOnCancellation {
            it?.let { cause ->
                if (cause is CancellationException) {
                    call.cancel()
                }
            }
        }
    }
}