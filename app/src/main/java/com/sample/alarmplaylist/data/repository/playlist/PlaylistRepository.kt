package com.sample.alarmplaylist.data.repository.playlist

import com.sample.alarmplaylist.playlist.playlist_db.Playlist

interface PlaylistRepository {
    suspend fun getAllPlaylists(): List<Playlist>
}