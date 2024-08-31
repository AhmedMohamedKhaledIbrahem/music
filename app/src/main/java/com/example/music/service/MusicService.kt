package com.example.music.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.music.App
import com.example.music.ExoPlayerActivity
import com.example.music.ExoPlayerUtilities
import com.example.music.MainActivity
import com.example.music.Music
import com.example.music.MusicUtilities
import com.example.music.R
import com.example.music.data.NavigationViewModel
import com.example.music.data.NotificationItemsViewModel
import kotlin.math.E

class MusicService : Service() {
private lateinit var exoPlayer :ExoPlayer


    override fun onCreate() {
        super.onCreate()
        exoPlayer =ExoPlayerUtilities.exoPlayer
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {



        when (intent?.action) {
            "ACTION_PLAY_PAUSE" -> {
                if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()

            }

            "ACTION_STOP" -> {

                exoPlayer.stop()
                stopSelf()
            }

            "ACTION_START" -> {
                val currentPosition = exoPlayer.currentPosition
                MusicUtilities.currentBuffer = currentPosition
                val mediaItem: MediaItem? = MusicUtilities.uri?.let { MediaItem.fromUri(it) }
                if (mediaItem != null) {
                    exoPlayer.setMediaItem(mediaItem)
                }

                exoPlayer.prepare()
                exoPlayer.seekTo(currentPosition)
                exoPlayer.playWhenReady = true

            }
            "ACTION_NEXT"->{


                if (exoPlayer.hasNextMediaItem()){
                    exoPlayer.seekToNext()
                }
            }
            "ACTION_PREV"->{
                if (exoPlayer.hasPreviousMediaItem()){
                    exoPlayer.seekToPreviousMediaItem()
                }

            }

            else -> {
               // val currentPosition = intent?.getLongExtra("CURRENT_POSITION", 0L)
                val mediaItem: MediaItem? = MusicUtilities.uri?.let { MediaItem.fromUri(it) }
                if (mediaItem != null) {
                    exoPlayer.setMediaItem(mediaItem)
                }
                exoPlayer.prepare()
                //if (currentPosition != null) {
                //    exoPlayer.seekTo(currentPosition)
              //  }
                exoPlayer.playWhenReady = true
            }
        }
        start()

        return START_NOT_STICKY
    }


    private fun start() {

      //  val currentPosition = exoPlayer.currentPosition
       /* val notificationIntent = Intent(this, ExoPlayerActivity::class.java)
        //.apply //{
          //  putExtra("CURRENT_POSITION", currentPosition)
        //}
        val pendingIntent = if (App.isAppInForeground){
            PendingIntent.getActivity(
                this, 0, Intent(), PendingIntent.FLAG_IMMUTABLE
            )
        }else{
            PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
        }*/
        val notificationIntent = Intent(this, ExoPlayerActivity::class.java).apply {
            putExtra("currentPosition",MusicUtilities.currentBuffer)
        }
        val pendingIntent =PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val remoteView = RemoteViews(packageName, R.layout.notification_custom).apply {

            setTextViewText(R.id.songTitleNotification, MusicUtilities.songTitle)
            setTextViewText(R.id.artistNameNotification, MusicUtilities.songArtist)

            var  nViewModel = MusicUtilities.appComponent?.let { ViewModelProvider(it) }?.get(NotificationItemsViewModel::class.java)
            val playPauseIcon =
                if (exoPlayer.isPlaying) R.drawable.baseline_play_arrow_24  else R.drawable.baseline_pause_24
            nViewModel?.changeNotificationItemStates(playPauseIcon)
            nViewModel?.updateItem?.observeForever { item ->
                setImageViewResource(R.id.buttonPlayPauseMusicNotification, item)
            }

            setOnClickPendingIntent(
                R.id.buttonPlayPauseMusicNotification,
                getActionIntent("ACTION_PLAY_PAUSE"),
            )
            setOnClickPendingIntent(
                R.id.buttonNextMusicNotification,
                getActionIntent("ACTION_NEXT")
            )
            setOnClickPendingIntent(
                R.id.buttonPreviousMusicNotification,
                getActionIntent("ACTION_PREV")
            )


        }


        val notification = NotificationCompat.Builder(this, "music_channel")
            .setSmallIcon(R.drawable.baseline_library_music_24)
            .setCustomContentView(remoteView)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        startForeground(1, notification)
    }

    private fun getActionIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onDestroy() {
      //  ExoPlayerUtilities.exoPlayer.release()
        super.onDestroy()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun formatTime(position: Long): String {
        val minutes = (position / 1000) / 60
        val seconds = (position / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


}