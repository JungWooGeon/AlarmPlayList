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
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.playlist.playlist_db.PlayList

class PlayListAdapter(
        private val applicationContext: Context,
        private val menuInflater: MenuInflater
    ): RecyclerView.Adapter<PlayListAdapter.MyViewHolder>() {

    interface AdapterListener {
        fun renamePlayList(pos: Int)
        fun deletePlayList(pos: Int)
    }

    var listener: AdapterListener? = null
    var list = ArrayList<PlayList>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgView: AppCompatImageView = itemView.findViewById(R.id.img_playlist)
        var playListTitle: TextView = itemView.findViewById(R.id.playlist_title)
        var btnMore: AppCompatImageView = itemView.findViewById(R.id.btn_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_playlist_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.imgView.setImageResource(R.drawable.playlist1)
        holder.playListTitle.text = list[position].playListTitle
        holder.btnMore.setOnClickListener { view ->
            val popupMenu = PopupMenu(applicationContext, view)
            menuInflater.inflate(R.menu.playlist_popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.rename_menu -> {
                        listener?.renamePlayList(position)
                    }
                    R.id.delete_menu -> {
                        listener?.deletePlayList(position)
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