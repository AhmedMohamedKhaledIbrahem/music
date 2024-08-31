package com.example.music

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FolderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    val folderIcon :ImageView = itemView.findViewById(R.id.folderIcon)
    val folderName : TextView = itemView.findViewById(R.id.folderName)
    val folderDetails:TextView = itemView.findViewById(R.id.folderDetails)
}