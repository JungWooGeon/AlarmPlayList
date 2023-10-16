package com.sample.alarmplaylist.data.repository.playlist

import com.sample.alarmplaylist.playlist.playlist_db.Playlist
import com.sample.alarmplaylist.playlist.playlist_db.PlaylistDataBase

class PlaylistRepositoryImpl(private val db: PlaylistDataBase) : PlaylistRepository {
    override suspend fun getAllPlaylists(): List<Playlist> {
        return db.playListDao().getAll()
    }
}