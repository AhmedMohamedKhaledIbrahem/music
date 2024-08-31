package com.example.music

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArtistViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    val firstCharacter :TextView = itemView.findViewById(R.id.characterOfArtist)
    val artistName:TextView = itemView.findViewById(R.id.artists)
    val numberOfSong :TextView = itemView.findViewById(R.id.numberOfSongs)
}