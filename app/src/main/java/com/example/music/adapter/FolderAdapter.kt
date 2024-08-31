package com.example.music.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.music.FolderViewHolder
import com.example.music.R
import com.example.music.data.FolderModelView

class FolderAdapter(
    private val folders: List<FolderModelView>,
    private val clickListener: (FolderModelView) -> Unit
    ) : RecyclerView.Adapter<FolderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_item,parent,false)
        return FolderViewHolder(view)
    }

    override fun getItemCount() = folders.size


    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folders[position]
        holder.folderName.text = folder.name
        holder.folderDetails.text = "$itemCount"
        holder.itemView.setOnClickListener { clickListener(folder) }
    }
}