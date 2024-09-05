package com.example.music.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.adapter.FolderAdapter
import com.example.music.data.model.FolderModel

class FolderFragment : Fragment() {
    private lateinit var folderRecyclerView: RecyclerView
    private lateinit var folderAdapter: FolderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_folder, container, false)
        folderRecyclerView = view.findViewById(R.id.folderRecyclerView)
        folderRecyclerView.layoutManager = LinearLayoutManager(context)
        loadFile()
        return view
    }

    private fun loadFile(){
        val folders = getFoldersFromStorage()
        folderAdapter = FolderAdapter(folders){
        folder ->
            val fragment = SongFragment().newInstance(folder.path)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer , fragment)
                .commit()
        }
        folderRecyclerView.adapter = folderAdapter

    }

    private fun getFoldersFromStorage(): List<FolderModel> {

        return listOf(
            FolderModel(name = "Music", details = " || /storage/emulated/0/Music/" , path = "/storage/emulated/0/Music/" )
        )
    }



}