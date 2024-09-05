package com.example.music.utils

import androidx.media3.exoplayer.ExoPlayer
import com.example.music.App

object ExoPlayerUtilities {
    var exoPlayer: ExoPlayer = ExoPlayer.Builder(App.instance).build()

}