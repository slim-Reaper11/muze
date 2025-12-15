package com.example.muze

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.muze.Player.MusicPlayer
import com.example.muze.data.LocalMusicRepository
import com.example.muze.data.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LocalMusicRepository(application)
    private val player = MusicPlayer(application)



    private val _playerState = MutableStateFlow(PlayerUiState())
    val playerState = _playerState.asStateFlow()


    val allSongs: StateFlow<List<Song>> =
        repository.getLocalSongs()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun play(song: Song) {
        player.playSong(song.filePath)
        _playerState.value = PlayerUiState(
            currentSong = song,
            isPlaying = true
        )

    }

    fun pause() {
        player.pause()
        _playerState.update { it.copy(isPlaying = false) }
    }

    fun resume() {
        player.resume()
        _playerState.update { it.copy(isPlaying = true) }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    fun getSongById(id: Long): Flow<Song?> =
        allSongs.map { list ->
            list.find { it.id == id }
        }
}

data class PlayerUiState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false
)