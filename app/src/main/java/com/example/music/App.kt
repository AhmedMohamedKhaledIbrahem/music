package com.example.music

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

class App:Application() ,LifecycleEventObserver {
    companion object{
        var isAppInForeground = false
        lateinit var instance: App
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val musicChannel = NotificationChannel(
                "music_channel",
                "Music Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(musicChannel)
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)


    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_START -> isAppInForeground = true
            Lifecycle.Event.ON_STOP -> isAppInForeground = false
            else->{}
        }
    }
}