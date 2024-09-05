package com.example.music.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R

class VideoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    val videoTitle :TextView = itemView.findViewById(R.id.video_title)
    val videoDescription :TextView = itemView.findViewById(R.id.video_description)
    val videoThumbnail :ImageView = itemView.findViewById(R.id.video_thumbnail)
}