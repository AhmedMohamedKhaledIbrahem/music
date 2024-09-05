package com.example.music.utils

import android.graphics.drawable.GradientDrawable
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

object MusicUtilities {
    var uri :String ? =null
    var gradientDrawable:GradientDrawable?=null
    var songTitle:String? = null
    var songArtist:String?=null
    var currentBuffer: Long? = null
    var appComponent:ComponentActivity?=null
}