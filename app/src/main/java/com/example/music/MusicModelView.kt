package com.example.music

import android.content.Context
import android.database.Cursor
import android.graphics.drawable.GradientDrawable
import android.os.Parcelable
import android.provider.MediaStore
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


data class MusicModelView(
    val uri: String = "",
    var musicTitle: String = "",
    var authorMusic: String = "",
    var  gradientDrawable:  GradientDrawable? = null

)


