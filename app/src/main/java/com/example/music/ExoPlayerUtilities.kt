package com.example.music

import androidx.media3.exoplayer.ExoPlayer

object ExoPlayerUtilities {
    var exoPlayer: ExoPlayer = ExoPlayer.Builder(App.instance).build()

}