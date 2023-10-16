package com.sample.alarmplaylist.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Youtube (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "videoId") val videoId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "playlistId") val playlistId: Int
)