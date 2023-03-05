package com.sample.alarmplaylist.alarm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sample.alarmplaylist.Constant
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.playlist.playlist_db.PlayList

/**
 * SelectPlaylistDialog 에 플레이리스트 정보들을 보여주는 RecyclerView 에서 사용하는 Adapter
 */
class SelectPlaylistAdapter : RecyclerView.Adapter<SelectPlaylistAdapter.MyViewHolder>() {

    var list = ArrayList<PlayList>()

    var selectedPosition = Constant.DEFAULT_SELECTED_POSITION
    private var selectedRadio: RadioButton? = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardLayout: ConstraintLayout = itemView.findViewById(R.id.card_layout)
        var imgPlaylist: AppCompatImageView = itemView.findViewById(R.id.img_playlist)
        var playlistTitle: TextView = itemView.findViewById(R.id.playlist_title)
        var btnRadio: RadioButton = itemView.findViewById(R.id.btn_radio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_select_playlist_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 플레이리스트 id 에 따라 image 를 다르게 보여줌
        when (list[position].id!! % Constant.PLAYLIST_IMAGE_COUNT) {
            0 -> holder.imgPlaylist.setImageResource(R.drawable.playlist1)
            1 -> holder.imgPlaylist.setImageResource(R.drawable.playlist2)
            2 -> holder.imgPlaylist.setImageResource(R.drawable.playlist3)
            3 -> holder.imgPlaylist.setImageResource(R.drawable.playlist4)
            4 -> holder.imgPlaylist.setImageResource(R.drawable.playlist5)
            5 -> holder.imgPlaylist.setImageResource(R.drawable.playlist6)
            6 -> holder.imgPlaylist.setImageResource(R.drawable.playlist7)
            7 -> holder.imgPlaylist.setImageResource(R.drawable.playlist8)
            8 -> holder.imgPlaylist.setImageResource(R.drawable.playlist9)
        }

        // 첫 번째 있는 플레이리스트는 체크 표시로 설정 (radio button 기본 설정)
        if (position == Constant.DEFAULT_SELECTED_POSITION) {
            holder.btnRadio.isChecked = true
            selectedRadio = holder.btnRadio
        }

        // 플레이리스트 클릭 시, 선택된 플레이리스트를 바꾸어 저장하고 radio button 설정
        holder.cardLayout.setOnClickListener {
            selectedRadio?.isChecked = false
            selectedPosition = position
            selectedRadio = holder.btnRadio
            holder.btnRadio.isChecked = true
        }

        holder.playlistTitle.text = list[position].playListTitle
    }

    override fun getItemCount(): Int {
        return list.size
    }
}