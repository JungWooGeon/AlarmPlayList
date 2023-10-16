package com.sample.alarmplaylist.data.repository.playlist

import com.sample.alarmplaylist.data.entity.Playlist

interface PlaylistRepository {
    suspend fun getAllPlaylists(): List<Playlist>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylist(id: Int)
}