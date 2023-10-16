package com.sample.alarmplaylist.domain.playlist

import com.sample.alarmplaylist.data.repository.playlist.PlaylistRepository

class DeletePlaylistUseCase(private val repository: PlaylistRepository) {
    suspend operator fun invoke(id: Int) {
        return repository.deletePlaylist(id)
    }
}