package com.example.music.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.music.ArtistViewHolder
import com.example.music.R
import com.example.music.data.ArtistModel

class ArtistAdapterRecycleView(
    private val aRecycleListView:  List<ArtistModel>,
    private val onClickListener: (ArtistModel) -> Unit
):RecyclerView.Adapter<ArtistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.artist_item,parent,false)
        return ArtistViewHolder(view)
    }

    override fun getItemCount() = aRecycleListView.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = aRecycleListView[position]
        holder.firstCharacter.text = artist.firstCharacterArtist
        holder.artistName.text = artist.artistName
        holder.numberOfSong.text = "${artist.numberOfSongs.size} songs"
        holder.itemView.setOnClickListener { onClickListener(artist) }
    }

}