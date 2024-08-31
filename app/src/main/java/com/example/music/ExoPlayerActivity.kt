package com.example.music

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.example.music.service.MusicService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExoPlayerActivity : AppCompatActivity() {
    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playPauseButton: ImageButton
    private lateinit var nextMusicButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var imageView: ImageView
    private lateinit var exoPlayerCard: CardView
    private lateinit var exoPlayerConstraintLayout: ConstraintLayout
    private lateinit var songTitleTextView: TextView
    private lateinit var songArtistTextView: TextView
    private lateinit var musicList: List<MusicModelView>


    companion object {
        const val EXTRA_MEDIA_URI = "extra_media_uri"
    }

    // lateinit var musicService: MusicService


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)
        exoPlayer = ExoPlayerUtilities.exoPlayer
        playerView = findViewById(R.id.player_view)
        playPauseButton = findViewById(R.id.buttonPlayPauseExoPlayer)
        nextMusicButton = findViewById(R.id.buttonNextExoPlayer)
        previousButton = findViewById(R.id.buttonPreviousExoPlayer)
        seekBar = findViewById(R.id.seekBarExoPlayer)
        imageView = findViewById(R.id.imageExoPlayer)
        exoPlayerConstraintLayout = findViewById(R.id.exoplayerConstraintlayout)
        exoPlayerCard = findViewById(R.id.exoPlayerCard)
        songTitleTextView = findViewById(R.id.songTitle)
        songArtistTextView = findViewById(R.id.artistName)
        //var currentPosition = intent.getLongExtra("currentPosition", 0L)

        //Log.d("ExoPlayerActivity2", "Received position: $currentPosition")
        musicList = getMusicList()


        initializePlayer(MusicUtilities.uri)
        backGround()
        songTitleAndArtist()
        playPauseExoPlayerListener()
        seekBarExoPlayerListener()
        //musicService.bindService(this) { service ->
        //  exoPlayer = service.getPlayer()}
        backPressed()
        updateSongInfo()


    }

    private fun getMusicList(): List<MusicModelView> {
        val sharedPreferences =
            applicationContext.getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("musicList", null)
        val type = object : TypeToken<List<MusicModelView>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }


    @OptIn(UnstableApi::class)
    private fun initializePlayer(uri: String?,) {
        val currentIndex = intent.getIntExtra("currentIndex", 0)
        ///exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        /*val mediaItem = uri?.let { MediaItem.fromUri(it) }
        if (mediaItem != null) {
            exoPlayer.setMediaItem(mediaItem)
        }*/


       val concatenatingMediaSource = createMediaSources(musicList)
       exoPlayer.setMediaSource(concatenatingMediaSource)

       /* if (currentIndex in musicList.indices){
            val music = musicList[currentIndex]
            val mediaItem = MediaItem.fromUri(music.uri)
            exoPlayer.setMediaItem(mediaItem)
        }*/


        exoPlayer.prepare()
        exoPlayer.seekTo(currentIndex,0)
        MusicUtilities.currentBuffer?.let {
            exoPlayer.pause()
            exoPlayer.seekTo(currentIndex,it)
            exoPlayer.play()
        }
        exoPlayer.playWhenReady = true
        updateSongInfo()
        // Log.e("exoPlayer.currentPosition","${exoPlayer.currentPosition.toInt()}")
        val intent = Intent(applicationContext, MusicService::class.java)
        stopService(intent)
    }

    @OptIn(UnstableApi::class)
    private fun createMediaSources(musicList: List<MusicModelView>): ConcatenatingMediaSource {
        val dataSourceFactory = DefaultDataSource.Factory(applicationContext)
        val concatenatingMediaSource = ConcatenatingMediaSource()
        val currentIndex = intent.getIntExtra("currentIndex", 0)
        musicList.forEachIndexed { index, music ->

                    Log.e("postion2", index.toString())
                    val mediaItem = MediaItem.fromUri(music.uri)
                    val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mediaItem)
                    concatenatingMediaSource.addMediaSource(mediaSource)


        }
        return concatenatingMediaSource
    }


    private fun playPauseExoPlayerListener() {
        playPauseButton.setOnClickListener {
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
                playPauseButton.setImageResource(R.drawable.baseline_play_arrow_24)
            } else {
                exoPlayer.play()
                playPauseButton.setImageResource(R.drawable.baseline_pause_24)
            }
        }
        nextMusicButton.setOnClickListener {

            if (exoPlayer.hasNextMediaItem()) {
                exoPlayer.seekToNextMediaItem()
            }
        }

        previousButton.setOnClickListener {
            if (exoPlayer.hasPreviousMediaItem()) {
                exoPlayer.seekToPreviousMediaItem()
            }
        }

    }

    private fun backGround() {

        exoPlayerCard.background = MusicUtilities.gradientDrawable
        exoPlayerConstraintLayout.background = getDrawable(R.drawable.shape_image)

    }

    private fun songTitleAndArtist() {
        songTitleTextView.text = MusicUtilities.songTitle
        songArtistTextView.text = MusicUtilities.songArtist
    }

    private fun seekBarExoPlayerListener() {

        exoPlayer.addListener(object : Player.Listener {

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    seekBar.max = exoPlayer.duration.toInt()
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                playPauseButton.setImageResource(
                    if (isPlaying) R.drawable.baseline_pause_24
                    else R.drawable.baseline_play_arrow_24
                )
            }

        })
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    exoPlayer.seekTo(progress.toLong())

                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.e("test", "test")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.e("test", "test")
            }

        })

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                seekBar.progress = exoPlayer.currentPosition.toInt()
                handler.postDelayed(this, 1000)
            }
        })

    }

    private fun backPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //val currentPosition = exoPlayer.currentPosition
                //MusicUtilities.currentBuffer = currentPosition
                finish()
                val intent = Intent(applicationContext, MusicService::class.java).apply {
                    action = "ACTION_START"
                }
                startForegroundService(intent)

            }


        })
    }
    private fun updateSongInfo(){
        exoPlayer.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val currentIndex = exoPlayer.currentMediaItemIndex
                if (currentIndex >= 0 && currentIndex < musicList.size) {
                    val music = musicList[currentIndex]
                    songTitleTextView.text = music.musicTitle
                    songArtistTextView.text = music.authorMusic
                    exoPlayerCard.background = music.gradientDrawable
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        //releasePlayer()
    }

    private fun releasePlayer() {
        ExoPlayerUtilities.exoPlayer.release()

    }



}