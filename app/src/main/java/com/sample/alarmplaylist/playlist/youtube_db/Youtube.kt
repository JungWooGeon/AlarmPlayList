package com.sample.alarmplaylist.playlist.youtube_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Youtube (
    @PrimaryKey @ColumnInfo(name = "videoId") val videoId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String
)