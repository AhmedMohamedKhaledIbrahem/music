package com.example.music.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.music.fragment.ArtistFragment
import com.example.music.fragment.SongFragment

class MusicTabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                SongFragment()
            }

            1 -> {
                ArtistFragment()
            }

            else -> {
                SongFragment()
            }
        }
    }


}