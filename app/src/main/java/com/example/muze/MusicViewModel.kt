package com.example.muze

import android.app.Application
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Queue

class MusicViewModel(
    application: Application,
) :
    AndroidViewModel(application) {


    private val controller = MusicController(application)
    private val repository = LocalMusicRepository(application)

    private var mediaController: MediaController? = null

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

    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode: StateFlow<Int> = _repeatMode.asStateFlow()

    private val _isTransitioning = MutableStateFlow(false)

    private val _mediaChanged = MutableStateFlow<Int>(0)
    val mediaChanged: StateFlow<Int> = _mediaChanged.asStateFlow()

    private val _originalQueue = MutableStateFlow<List<Song>>(emptyList())

    private val _shuffledQueue = MutableStateFlow<List<Song>>(emptyList())

    private val _queue = MutableStateFlow<List<Song>>(emptyList())
    val queue = _queue.asStateFlow()

    private val _isShuffled = MutableStateFlow(false)
    val isShuffled: StateFlow<Boolean> = _isShuffled.asStateFlow()

    private val _currentMediaId = MutableStateFlow<String?>(null)

    init {

        controller.controllerFuture.addListener(
            {
                mediaController = controller.controllerFuture.get()
                observePlayer()
            },
            MoreExecutors.directExecutor()
        )

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
                if (!_isTransitioning.value) {
                    _isPlaying.value = isPlayingNow
                }
            }

            override fun onMediaItemTransition(
                mediaItem: MediaItem?,
                reason: Int
            ) {
                _currentMediaId.value = mediaItem?.mediaId
                val song = allSongs.value.find { it.id == _currentMediaId.value?.toLongOrNull() }
                _currentSong.value = song
                _currentPosition.value = 0L
                _isTransitioning.value = true
            }

            override fun onPlaybackStateChanged(state: Int) {
                _playerState.value = state

                if (state == Player.STATE_READY) {
                    // duration is now valid (we'll use this later)
                    val duration = mediaController?.duration
                    if (duration != null) {
                        _duration.value = duration
                    }
                    _isTransitioning.value = false
                }

            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                if (reason == Player.DISCONTINUITY_REASON_SEEK) {
                    _isTransitioning.value = true
                }
            }


            override fun onRepeatModeChanged(repeatMode: Int) {
                super.onRepeatModeChanged(repeatMode)
                _repeatMode.value = repeatMode
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

    fun createPlaylist(songs: List<Song>, index: Int) {
        val mediaItems = songs.map { it.toMediaItem() }
        mediaController?.setMediaItems(mediaItems, index, 0)
    }

    fun play(songs: List<Song>, index: Int) {
        _originalQueue.value = songs
        _queue.value = _originalQueue.value
        createPlaylist(songs, index)
//        val mediaItems = songs.map { song ->
//
//            MediaItem.Builder()
//                .setMediaId(song.id.toString()) // Use the song ID as the Media ID
//                .setUri(song.filePath)
//                .setMediaMetadata(
//                    MediaMetadata.Builder()
//                        .setTitle(song.title)
//                        .setArtist(song.artist)
//                        // You can add other useful metadata here if needed
//                        .build()
//                )
//                .build()
//        }
//
//        mediaController?.setMediaItems(mediaItems, index, 0)
//        mediaController?.shuffleModeEnabled = isShuffled.value
        mediaController?.prepare()
        mediaController?.play()
    }

    fun pause() {
        mediaController?.pause()
    }


    fun resume() {
        mediaController?.play()
    }


    fun nextSong() {
        mediaController?.seekToNext()
    }

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
        mediaController?.let { player ->
            val currentIndex = player.currentMediaItemIndex
            val count = player.mediaItemCount
            _isShuffled.value = !_isShuffled.value
            if (_isShuffled.value) {
                _shuffledQueue.value = _originalQueue.value.shuffled()
                if (currentIndex + 1 < count) {
                    if (currentIndex != 0) {
                        player.removeMediaItems(0, currentIndex)
                    }
                    player.removeMediaItems(currentIndex + 1, count)
                }
                _queue.value = (listOf(_currentSong.value) + _shuffledQueue.value) as List<Song>
                player.addMediaItems(1, _shuffledQueue.value.map { it.toMediaItem() })
            } else {
                val songId = player.currentMediaItem?.mediaId?.toLong()
                val songIndex =
                    _originalQueue.value.indexOf(_originalQueue.value.find { it.id == songId })
                if (currentIndex + 1 < count) {
                    if (currentIndex != 0) {
                        player.removeMediaItems(0, currentIndex)
                    }
                    player.removeMediaItems(currentIndex + 1, count)
                }
                player.addMediaItems(_originalQueue.value.map { it.toMediaItem() })
                player.removeMediaItem(songIndex + 1)
                player.moveMediaItem(0, songIndex)
                _queue.value = _originalQueue.value
            }
        }
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

    fun Song.toMediaItem(): MediaItem =
        MediaItem.Builder()
            .setMediaId(id.toString())
            .setUri(filePath)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setArtist(artist)
                    .build()
            ).build()
}



