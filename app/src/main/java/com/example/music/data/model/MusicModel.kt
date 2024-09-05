package com.example.music.data.model

import android.graphics.drawable.GradientDrawable


data class MusicModel(
    val uri: String = "",
    var musicTitle: String = "",
    var authorMusic: String = "",
    var  gradientDrawable:  GradientDrawable? = null

)


