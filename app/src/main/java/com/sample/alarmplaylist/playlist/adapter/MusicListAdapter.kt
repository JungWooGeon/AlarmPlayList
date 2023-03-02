package com.sample.alarmplaylist.playlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.playlist.youtube_db.Youtube

class MusicListAdapter(
    private val context: Context,
    private val menuInflater: MenuInflater
    ) : RecyclerView.Adapter<MusicListAdapter.MyViewHolder>() {

    interface AdapterListener {
        fun deleteMusic(pos: Int)
    }

    var listener: AdapterListener? = null
    var list = ArrayList<Youtube>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgMusic: AppCompatImageView = itemView.findViewById(R.id.img_music)
        var musicTitle: TextView = itemView.findViewById(R.id.music_title)
        var btnMore: AppCompatImageView = itemView.findViewById(R.id.btn_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_musiclist_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(list[position].thumbnail).into(holder.imgMusic)

        holder.musicTitle.text = list[position].title

        holder.btnMore.setOnClickListener { view ->
            val popupMenu = PopupMenu(context, view)
            menuInflater.inflate(R.menu.musiclist_popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete_menu -> {
                        listener?.deleteMusic(position)
                    }
                }
                false
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}