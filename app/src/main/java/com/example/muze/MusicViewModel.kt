package com.example.muze

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.example.muze.Player.MusicPlayer
import com.example.muze.data.LocalMusicRepository
import com.example.muze.data.Song
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LocalMusicRepository(application)
    private val player = MusicPlayer(application)


    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _duration = MutableStateFlow<Long>(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _currentPosition = MutableStateFlow<Long>(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _playerState = MutableStateFlow<Int?>(null)

    private val _isChanging = MutableStateFlow<Boolean>(false)


    init {
        player.onIsPlayingChanged = { playing ->
            _isPlaying.value = playing
        }

        player.onMediaItemChanged = { mediaItem ->
            val songId = mediaItem?.mediaId?.toLongOrNull()
            val song = allSongs.value.find { it.id == songId }
            _currentSong.value = song
        }

        player.onPlaybackStateChanged = { state ->
            _playerState.value = state

            if (state == Player.STATE_READY) {
                // duration is now valid (we'll use this later)
                val duration = player.duration()
                _duration.value = duration
                // store it later when we add progress
//                _currentPosition.value = 0L

            }
        }

        viewModelScope.launch {
            while (isActive) {
                if (_playerState.value == Player.STATE_READY && _isPlaying.value && !_isChanging.value) {
                    _currentPosition.value = player.currentPosition()
                }
                delay(500)
            }
        }
    }


    val allSongs: StateFlow<List<Song>> =
        repository.getLocalSongs()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun play(songs: List<Song>, index: Int) {
        player.playSong(songs, index)
    }

    fun pause() {
        player.pause()
    }

    fun resume() {
        player.resume()
    }

    fun nextSong() {
        player.nextSong()
    }

    fun previousSong() {
        player.previousSong()
    }


    fun updateCurrentPosition(position: Long) {
        _currentPosition.value = position
    }

    fun goToCurrentPosition(position: Long) {
        player.seekTo(position)
    }

    fun updateIsChanging(bool: Boolean) {
        _isChanging.value = bool
    }


    override fun onCleared() {
        super.onCleared()
        player.release()
    }


}



