package com.sample.alarmplaylist.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "playListTitle") var playListTitle: String
)