package com.sample.alarmplaylist.data.repository.playlist

import com.sample.alarmplaylist.data.entity.Playlist
import com.sample.alarmplaylist.data.db.playlist.PlaylistDataBase

class PlaylistRepositoryImpl(private val db: PlaylistDataBase) : PlaylistRepository {
    override suspend fun getAllPlaylists(): List<Playlist> {
        return db.playListDao().getAll()
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        return db.playListDao().insertPlayList(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        return db.playListDao().updatePlayList(playlist)
    }

    override suspend fun deletePlaylist(id: Int) {
        return db.playListDao().deletePlaylistById(id)
    }
}