package com.example.muze.Player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class MusicPlayer(val context: Context){
    private val player = ExoPlayer.Builder(context).build()

    fun playSong(path: String) {
        val mediaItem = MediaItem.fromUri(path)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }
    fun pause() = player.pause()
    fun resume() = player.play()
    fun stop() = player.stop()

    fun release() = player.release()

    val isPlaying get() = player.isPlaying
}