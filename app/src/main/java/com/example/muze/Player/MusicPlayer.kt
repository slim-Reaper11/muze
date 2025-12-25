package com.example.muze.Player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import com.example.muze.data.Song
import androidx.media3.common.Player
import kotlinx.coroutines.delay


class MusicPlayer(val context: Context) {
    private val player = ExoPlayer.Builder(context).build()
    private var currentPlaylist: List<Song> = emptyList()

    var onIsPlayingChanged: ((Boolean) -> Unit)? = null
    var onMediaItemChanged: ((MediaItem?) -> Unit)? = null
    var onPlaybackStateChanged: ((Int) -> Unit)? = null




    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                onIsPlayingChanged?.invoke(isPlaying)
            }
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                onMediaItemChanged?.invoke(mediaItem)
            }

            override fun onPlaybackStateChanged(state: Int) {
                onPlaybackStateChanged?.invoke(state)
            }
        })

    }

    fun playSong(songs: List<Song>, index: Int) {
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
        currentPlaylist = songs

        player.setMediaItems(mediaItems, index, 0)
        player.prepare()
        player.play()
    }



    fun pause() = player.pause()
    fun nextSong() = player.seekToNext()
    fun previousSong() = player.seekToPrevious()
    fun seekTo(position: Long) = player.seekTo(position)
    fun resume() = player.play()
    fun release() = player.release()


    fun duration(): Long = player.duration

    fun currentPosition(): Long = player.currentPosition




}


