package com.sample.alarmplaylist.domain.playlist

import com.sample.alarmplaylist.data.repository.playlist.PlaylistRepository
import com.sample.alarmplaylist.data.entity.Playlist

class GetAllPlaylistsUseCase(private val repository: PlaylistRepository) {
    suspend operator fun invoke(): List<Playlist> {
        return repository.getAllPlaylists()
    }
}