package com.example.music.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.example.music.R
import com.example.music.data.model.MusicModel
import com.example.music.utils.ExoPlayerUtilities
import com.example.music.utils.MusicUtilities
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MusicService : Service() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var musicList: List<MusicModel>


    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayerUtilities.exoPlayer
        musicList = getMusicList()


        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                start()
            }

            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                start()
            }

        })


    }


    @OptIn(UnstableApi::class)
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
                val currentIndex = intent.getIntExtra("currentIndex", 0)
                val currentPosition = intent.getLongExtra("currentPosition", 0)
                val concatenatingMediaSource = createMediaSources(musicList)
                exoPlayer.setMediaSource(concatenatingMediaSource)
                exoPlayer.prepare()
                exoPlayer.seekTo(currentIndex, currentPosition)
                exoPlayer.playWhenReady = true

            }

            "ACTION_NEXT" -> {


                if (exoPlayer.hasNextMediaItem()) {
                    exoPlayer.seekToNext()
                } else {
                    exoPlayer.seekTo(0)
                }
            }

            "ACTION_PREV" -> {
                if (exoPlayer.hasPreviousMediaItem()) {
                    exoPlayer.seekToPreviousMediaItem()
                } else {
                    exoPlayer.seekTo(0)
                }

            }

            else -> {
                val mediaItem: MediaItem? = MusicUtilities.uri?.let { MediaItem.fromUri(it) }
                if (mediaItem != null) {
                    exoPlayer.setMediaItem(mediaItem)
                }
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
            }
        }
        start()

        return START_NOT_STICKY
    }


    private fun start() {


        val remoteView = RemoteViews(packageName, R.layout.notification_custom).apply {
            val currentIndex = exoPlayer.currentMediaItemIndex
            setTextViewText(R.id.songTitleNotification, musicList[currentIndex].musicTitle)
            setTextViewText(R.id.artistNameNotification, musicList[currentIndex].authorMusic)
            /*var  nViewModel = MusicUtilities.appComponent?.let { ViewModelProvider(it) }?.get(
                NotificationItemsViewModel::class.java)
            val playPauseIcon =
                if (exoPlayer.isPlaying) R.drawable.baseline_play_arrow_24  else R.drawable.baseline_pause_24
            nViewModel?.changeNotificationItemStates(playPauseIcon)
            nViewModel?.updateItem?.observeForever { item ->
                setImageViewResource(R.id.buttonPlayPauseMusicNotification, item)
            }*/
            val playPauseIcon = if (exoPlayer.isPlaying) {

                R.drawable.baseline_pause_24

            } else {
                R.drawable.baseline_play_arrow_24


            }

            setImageViewResource(R.id.buttonPlayPauseMusicNotification, playPauseIcon)

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
            .setOngoing(true)
            .build()
        startForeground(1, notification)
    }

    private fun getActionIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onDestroy() {
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

    @OptIn(UnstableApi::class)
    fun createMediaSources(musicList: List<MusicModel>): ConcatenatingMediaSource {
        val dataSourceFactory = DefaultDataSource.Factory(applicationContext)
        val concatenatingMediaSource = ConcatenatingMediaSource()
        musicList.forEachIndexed { index, music ->
            val mediaItem = MediaItem.fromUri(music.uri)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    private fun getMusicList(): List<MusicModel> {
        val sharedPreferences =
            applicationContext.getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("musicList", null)
        val type = object : TypeToken<List<MusicModel>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }


}