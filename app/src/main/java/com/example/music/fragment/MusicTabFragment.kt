package com.example.music.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.music.R
import com.example.music.adapter.MusicTabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MusicTabFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music_tab, container, false)
        val viewPager: ViewPager2 = view.findViewById(R.id.musicViewPager)
        val tabLayout: TabLayout = view.findViewById(R.id.musicTapsLayout)
        val musicAdapter = MusicTabAdapter(requireActivity())
        // musicTapsLayout(tabLayout)
        // musicAdapterListener(musicAdapter, viewPager, tabLayout)
        viewPager.adapter = musicAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Songs"
                1 -> "Artists"
                2 -> "Albums"
                else -> null
            }
        }.attach()


        return view
    }


  /*  private fun musicTapsLayout(tabLayout: TabLayout) {
        tabLayout.addTab(tabLayout.newTab().setText("Songs"))
        tabLayout.addTab(tabLayout.newTab().setText("Artists"))
        tabLayout.addTab(tabLayout.newTab().setText("Albums"))
        tabLayout.addTab(tabLayout.newTab().setText("Folders"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

    }

    private fun musicAdapterListener(
        musicTabAdapter: MusicTabAdapter,
        viewPager: ViewPager2,
        tabLayout: TabLayout
    ) {
        viewPager.adapter = musicTabAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tap: TabLayout.Tab?) {

                if (tap != null) {
                    viewPager.currentItem = tap.position
                }

            }

            override fun onTabUnselected(tap: TabLayout.Tab?) {
                tap?.position
            }

            override fun onTabReselected(tap: TabLayout.Tab?) {
                tap?.position
            }

        })
    }*/


}