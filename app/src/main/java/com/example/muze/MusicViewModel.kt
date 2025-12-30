package com.example.muze

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.session.MediaController
import com.example.muze.data.LocalMusicRepository
import com.example.muze.data.Song
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MusicViewModel(
    application: Application,

    ) :
    AndroidViewModel(application) {


    private val controller = MusicController(application)
    private val repository = LocalMusicRepository(application)
//    private val player = MusicPlayer(application)

    private var mediaController: MediaController? = null
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isNotPaused = MutableStateFlow(false)
    val isNotPaused: StateFlow<Boolean> = _isNotPaused.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _duration = MutableStateFlow<Long>(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _currentPosition = MutableStateFlow<Long>(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _playerState = MutableStateFlow<Int?>(null)

    private val _isChanging = MutableStateFlow<Boolean>(false)

//    private var currentPlaylist: List<Song> = emptyList()


    private val _isShuffled = MutableStateFlow(false)
    val isShuffled: StateFlow<Boolean> = _isShuffled.asStateFlow()

    private val _repeatMode =
        MutableStateFlow(Player.REPEAT_MODE_OFF)

    val repeatMode: StateFlow<Int> = _repeatMode.asStateFlow()

    private var playlistSet = false


    init {

        controller.controllerFuture.addListener(
            {
                mediaController = controller.controllerFuture.get()
                observePlayer()
            },
            MoreExecutors.directExecutor()
        )

//        player.onIsPlayingChanged = { playing ->
//            _isPlaying.value = playing
//        }
//
//        player.onMediaItemChanged = { mediaItem ->
//            val songId = mediaItem?.mediaId?.toLongOrNull()
//            val song = allSongs.value.find { it.id == songId }
//            _currentSong.value = song
//            _currentPosition.value = 0L
//        }
//
//        player.onPlaybackStateChanged = { state ->
//            _playerState.value = state
//
//            if (state == Player.STATE_READY) {
//                // duration is now valid (we'll use this later)
//                val duration = player.duration()
//                _duration.value = duration
//                // store it later when we add progress
////                _currentPosition.value = 0L
//
//            }
//        }

        viewModelScope.launch {
            while (isActive) {
                if (_playerState.value == Player.STATE_READY && _isPlaying.value && !_isChanging.value) {
                    mediaController?.let {
                        _currentPosition.value = it.currentPosition
                    }
                }
                delay(500)
            }
        }
    }

    private fun observePlayer() {
        mediaController?.addListener(object : Player.Listener {

            override fun onIsPlayingChanged(isPlayingNow: Boolean) {
                _isPlaying.value = isPlayingNow
            }

            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int
            ) {
                val songId = mediaItem?.mediaId?.toLongOrNull()
                val song = allSongs.value.find { it.id == songId }
                _currentSong.value = song
                _currentPosition.value = 0L
            }

            override fun onPlaybackStateChanged(state: Int) {
                _playerState.value = state

                if (state == Player.STATE_READY) {
                    // duration is now valid (we'll use this later)
                    val duration = mediaController?.duration
                    if (duration != null) {
                        _duration.value = duration
                    }
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                super.onRepeatModeChanged(repeatMode)
                _repeatMode.value = repeatMode
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                _isShuffled.value = shuffleModeEnabled
            }
        })
    }


    val allSongs: StateFlow<List<Song>> =
        repository.getLocalSongs()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


//        fun play(songs: List<Song>, index: Int) {
//        player.playSong(songs, index)
//        _isNotPaused.value = true
//    }
    fun play(songs: List<Song>, index: Int) {
        val mediaItems = songs.map { song ->

            MediaItem.Builder()
                .setMediaId(song.id.toString()) // Use the song ID as the Media ID
                .setUri(song.filePath)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        // You can add other useful metadata here if needed
                        .build()
                )
                .build()
        }

        mediaController?.setMediaItems(mediaItems, index, 0)
        mediaController?.shuffleModeEnabled = isShuffled.value
        mediaController?.prepare()
        mediaController?.play()
        _isNotPaused.value = true
    }


//    fun play(songs: List<Song>, index: Int) {
//
//        if (!playlistSet) {
//            val mediaItems = songs.map { song ->
//                MediaItem.Builder()
//                    .setMediaId(song.id.toString())
//                    .setUri(song.filePath)
//                    .setMediaMetadata(
//                        MediaMetadata.Builder()
//                            .setTitle(song.title)
//                            .setArtist(song.artist)
//                            .build()
//                    )
//                    .build()
//            }
//
//            mediaController?.setMediaItems(mediaItems)
//            mediaController?.prepare()
//            mediaController?.repeatMode = _repeatMode.value
//            playlistSet = true
//        }
//
//        mediaController?.seekTo(index, 0)
//        mediaController?.play()
//        _isNotPaused.value = true
//    }


    //    fun pause() {
//        player.pause()
//        _isNotPaused.value = false
//    }
    fun pause() {
        mediaController?.pause()
        _isNotPaused.value = false
    }


//    fun resume() {
//        player.resume()
//        _isNotPaused.value = true
//    }

    fun resume() {
        mediaController?.play()
        _isNotPaused.value = true
    }


    //    fun nextSong() {
//        player.nextSong()
//    }
    fun nextSong() {
        mediaController?.seekToNext()
    }

    //    fun previousSong() {
//        player.previousSong()
//    }
    fun previousSong() {
        mediaController?.seekToPrevious()
    }


    fun updateCurrentPosition(position: Long) {
        _currentPosition.value = position
    }

    fun goToCurrentPosition(position: Long) {
        mediaController?.seekTo(position)
    }

    fun updateIsChanging(bool: Boolean) {
        _isChanging.value = bool
    }

    fun shuffleMode() {
        _isShuffled.value = !_isShuffled.value
        mediaController?.shuffleModeEnabled = _isShuffled.value
    }

    fun toggleRepeatMode() {
        val nextMode = when (_repeatMode.value) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_OFF
            else -> Player.REPEAT_MODE_OFF
        }

        _repeatMode.value = nextMode
        mediaController?.repeatMode = nextMode
    }


    override fun onCleared() {
        super.onCleared()
        mediaController?.release()
    }


}



