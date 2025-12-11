package com.example.muze

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.muze.Player.MusicPlayer
import com.example.muze.data.LocalMusicRepository
import com.example.muze.data.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LocalMusicRepository(application)
    private val player = MusicPlayer(application)

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs = _songs.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong = _currentSong.asStateFlow()

    fun loadSongs() {
        viewModelScope.launch {
            _songs.value = repository.getSongs()
        }
    }

    fun play(song: Song) {
        _currentSong.value = song
        player.playSong(song.filePath)
    }

    fun pause() = player.pause()
    fun resume() = player.resume()

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}