package com.sample.alarmplaylist.domain.playlist

import com.sample.alarmplaylist.data.entity.Playlist
import com.sample.alarmplaylist.data.repository.playlist.PlaylistRepository

class UpdatePlaylistUseCase(private val repository: PlaylistRepository) {
    suspend operator fun invoke(playlist: Playlist) {
        return repository.updatePlaylist(playlist)
    }
}