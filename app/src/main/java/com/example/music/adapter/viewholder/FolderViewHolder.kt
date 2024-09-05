package com.example.music.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R

class FolderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    val folderIcon :ImageView = itemView.findViewById(R.id.folderIcon)
    val folderName : TextView = itemView.findViewById(R.id.folderName)
    val folderDetails:TextView = itemView.findViewById(R.id.folderDetails)
}