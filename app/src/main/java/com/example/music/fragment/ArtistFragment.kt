package com.example.music.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.adapter.ArtistAdapterRecycleView
import com.example.music.adapter.getArtistFile

class ArtistFragment : Fragment() {
    private lateinit var artistRecyclerView: RecyclerView
    private lateinit var artistAdapter: ArtistAdapterRecycleView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_artist, container, false)
        artistRecyclerView = view.findViewById(R.id.artistRecycleView)

        recycleCrate()
        return view
    }

    private fun recycleCrate(){
        val artist = getArtistFile(requireContext())
        artistRecyclerView.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        artistAdapter = ArtistAdapterRecycleView(artist){}
        artistRecyclerView.adapter = artistAdapter
    }


}