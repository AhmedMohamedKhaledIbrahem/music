package com.example.music.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.music.MusicModelView
import com.example.music.MusicViewHolder
import com.example.music.R

class MusicAdapterRecycleView(
    private var context: Context?,
    private var mRecycleListView:List<MusicModelView>,
    private val onMusicClick:(MusicModelView , Int) -> Unit
)
    :RecyclerView.Adapter<MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val musicList :View = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_item_list,parent,false)
        return MusicViewHolder(musicList)
    }

    override fun getItemCount(): Int {
        return mRecycleListView.size
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val musicInformationView = mRecycleListView[position]
        val gradientDrawable = createGradientDrawable(position)
        holder.imageView.background =  gradientDrawable
        holder.musicTitleView.text = musicInformationView.musicTitle
        holder.authorMusicView.text = musicInformationView.authorMusic
        musicInformationView.gradientDrawable = gradientDrawable
        holder.musicCardVIew.setOnClickListener { onMusicClick(musicInformationView,position) }
        holder.shareButton.setOnClickListener {
            shareMusic(musicInformationView)

        }
        holder.popMenuButton.setOnClickListener{
            showPopupMenu(it, position)

        }
    }

    private fun shareMusic(musicInformationView: MusicModelView) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "audio/mp3"
            putExtra(Intent.EXTRA_STREAM, musicInformationView.uri.toUri())
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

        }
        context?.startActivity(Intent.createChooser(intent, "Share Music"))
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(context,view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.pop_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            handleMenuItemClick(menuItem,position)
        }
        popupMenu.show()
    }

    private fun handleMenuItemClick(menuItem: MenuItem, position: Int):Boolean {
       val musicInformationView = mRecycleListView[position]
        return when(menuItem.itemId){
            R.id.action_play_later -> {
                Toast.makeText(context,"playLater",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_add_to_queue -> {
                Toast.makeText(context,"addQueue",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_add_to_playlist -> {
                Toast.makeText(context,"PlayList",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_share ->{
                shareMusic(musicInformationView)
                true
            }
            else -> false
        }
    }

    private fun createGradientDrawable(imageColor:Int):GradientDrawable{
        val startColor = when(imageColor % 3){
            0-> hexToColor("#90caf9") //blue
            1-> hexToColor("#4dd0e1") //cyan
            else -> hexToColor("#5c6bc0") //indigo
        }
        val endColor = when(imageColor%3){
            0-> hexToColor("#81c784") //green
            1-> hexToColor("#4db6ac") //teal
            else -> hexToColor("#7e57c2") //deepPurple
        }
        val gradientDrawable =GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(startColor,endColor))
        gradientDrawable.cornerRadius = 40f

        return  gradientDrawable



    }

    private fun hexToColor(hex: String): Int {
        return Color.parseColor(hex)
    }
}