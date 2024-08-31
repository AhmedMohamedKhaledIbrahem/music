package com.example.music.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.ExoPlayerActivity
import com.example.music.MusicModelView
import com.example.music.MusicUtilities
import com.example.music.R
import com.example.music.adapter.MusicAdapterRecycleView
import com.example.music.adapter.getMusicFile
import com.google.gson.Gson
import java.io.File


class SongFragment : Fragment() {
    private lateinit var mRecyclerView: RecyclerView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            createRecycleList(mRecyclerView)
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestPermissionLauncher2 =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.entries.all { it.value }
            if (allPermissionsGranted) {
                // All permissions granted, proceed with your logic
                createRecycleList(mRecyclerView)
            } else {
                // Permissions denied, handle the case
                Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_play_music, container, false)
        mRecyclerView = view.findViewById(R.id.musicRecycleView)

        if (isReadStoragePermissionGranted()) {
            createRecycleList(mRecyclerView)
        } else {
            requestReadStoragePermission()
        }





        return view
    }

    private fun isReadStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadStoragePermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    private fun createRecycleList(recyclerView: RecyclerView) {
        val musicList = getMusicFile(requireContext())
        saveMusicList(musicList)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val customAdapter = MusicAdapterRecycleView(context, musicList) { musicData, position ->
            Log.e("postion", position.toString())
            MusicUtilities.uri = musicData.uri
            MusicUtilities.gradientDrawable = musicData.gradientDrawable
            MusicUtilities.songTitle = musicData.musicTitle
            MusicUtilities.songArtist = musicData.authorMusic
            openExoPlayerActivity(position)

        }
        recyclerView.adapter = customAdapter


    }




    private fun saveMusicList(musicList: List<MusicModelView>) {
        val shardPreferences =
            requireContext().getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE)
        val editor = shardPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(musicList)
        editor.putString("musicList", json)
        editor.apply()
    }

    companion object {
        private val music_PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,


            )
    }

    private fun hasRequiredPermissionsMusic(): Boolean {

        return music_PERMISSIONS.all {

            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }


    }


    private fun openExoPlayerActivity(position: Int) {
        val intent = Intent(requireContext(), ExoPlayerActivity::class.java).apply {
            putExtra("currentIndex", position)
        }
        startActivity(intent)
    }

    private fun createGradientDrawable(imageColor: Int): GradientDrawable {
        val startColor = when (imageColor % 3) {
            0 -> hexToColor("#90caf9") //blue
            1 -> hexToColor("#4dd0e1") //cyan
            else -> hexToColor("#5c6bc0") //indigo
        }
        val endColor = when (imageColor % 3) {
            0 -> hexToColor("#81c784") //green
            1 -> hexToColor("#4db6ac") //teal
            else -> hexToColor("#7e57c2") //deepPurple
        }
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(startColor, endColor)
        )
        gradientDrawable.cornerRadius = 40f

        return gradientDrawable


    }

    private fun hexToColor(hex: String): Int {
        return Color.parseColor(hex)
    }

    fun newInstance(folderPath: String) =
        SongFragment().apply {
            arguments = Bundle().apply {
                putString("folderPath", folderPath)
            }
        }


}