package com.sample.alarmplaylist.playlist.api_retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchVideoInterface {

    @GET("search")
    fun getSearchResult(
        @Query("part") part: String,
        @Query("type") type: String,
        @Query("key") key: String,
        @Query("q") q: String
    ) : Call<SearchVideoResult>
}