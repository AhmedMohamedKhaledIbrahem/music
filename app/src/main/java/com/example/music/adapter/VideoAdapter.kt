package com.example.music.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music.R
import com.example.music.adapter.viewholder.VideoViewHolder
import com.example.music.data.model.ItemModel

class VideoAdapter(
    private val videoList: List<ItemModel>
) : RecyclerView.Adapter<VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_item, parent, false)
        return VideoViewHolder(view)
    }


    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.videoTitle.text = video.snippet.title
        holder.videoDescription.text = video.snippet.description
        Glide.with(holder.itemView.context)
            .load(video.snippet.thumbnails.default.url)
            .into(holder.videoThumbnail)

        holder.itemView.setOnClickListener {
            val videoId = video.id.videoId
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
       return videoList.size
    }
}