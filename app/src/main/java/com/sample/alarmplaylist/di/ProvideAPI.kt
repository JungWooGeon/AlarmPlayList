package com.sample.alarmplaylist.di

import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.data.network.SearchYoutubeInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideSearchYoutubeRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constants.RETROFIT_SEARCH_YOUTUBE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideSearchYoutubeInterface(retrofit: Retrofit): SearchYoutubeInterface {
    return retrofit.create(SearchYoutubeInterface::class.java)
}