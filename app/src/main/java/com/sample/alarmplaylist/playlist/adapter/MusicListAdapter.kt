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
import com.sample.alarmplaylist.data.entity.Youtube

/**
 * PlaylistFragment 음악리스트 recycler view adapter
 */
class MusicListAdapter(
    private val context: Context,
    private val menuInflater: MenuInflater
) : RecyclerView.Adapter<MusicListAdapter.MyViewHolder>() {

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
        // Glide 를 사용하여 썸네일 이미지를 가져와서 반영
        Glide.with(context).load(list[position].thumbnail).into(holder.imgMusic)

        holder.musicTitle.text = list[position].title

        // 더보기 클릭하여 나온 menu 에서 삭제 선택 시 listener 를 통해 삭제 기능 수행
        holder.btnMore.setOnClickListener { view ->
            val popupMenu = PopupMenu(context, view)
            menuInflater.inflate(R.menu.musiclist_popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete_menu -> {
                        listener?.deleteMusic(list[holder.absoluteAdapterPosition].id!!)
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

    // PlaylistFragment 와 통신하기 위한 listener
    interface AdapterListener {
        fun deleteMusic(id: Int)
    }
}