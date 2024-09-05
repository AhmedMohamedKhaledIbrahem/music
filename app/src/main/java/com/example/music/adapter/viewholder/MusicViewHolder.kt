package com.example.music.adapter.viewholder

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R

class MusicViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView) {

    val imageView:ImageView = itemView.findViewById(R.id.musicImage)
    val musicTitleView :TextView = itemView.findViewById(R.id.musicTitle)
    val authorMusicView : TextView = itemView.findViewById(R.id.authorMusic)
    val shareButton:ImageButton = itemView.findViewById(R.id.shareButton)
    val popMenuButton :ImageButton = itemView.findViewById(R.id.popMenuButton)
    val musicCardVIew :CardView = itemView.findViewById(R.id.musicCardView)
}