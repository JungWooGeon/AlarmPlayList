package com.sample.alarmplaylist.presentation.playlist

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.data.entity.Playlist

/**
 * PlaylistFragment 플레이리스트 recycler view adapter
 */
class PlayListAdapter(
        private val applicationContext: Context,
        private val menuInflater: MenuInflater
    ): RecyclerView.Adapter<PlayListAdapter.MyViewHolder>() {

    companion object {
        private const val DEFAULT_IMAGE_VIEW_ALPHA = 1F
        private const val SELECTED_IMAGE_VIEW_ALPHA = 0.5F
    }

    var listener: AdapterListener? = null
    var list = ArrayList<Playlist>()
    var selectedImgView: AppCompatImageView? = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardLayout: ConstraintLayout = itemView.findViewById(R.id.card_layout)
        var cardView: CardView = itemView.findViewById(R.id.cardview)
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
        // 플레이리스트 이미지 설정
        when (list[position].id!! % Constants.PLAYLIST_IMAGE_COUNT) {
            0 -> holder.imgView.setImageResource(R.drawable.playlist1)
            1 -> holder.imgView.setImageResource(R.drawable.playlist2)
            2 -> holder.imgView.setImageResource(R.drawable.playlist3)
            3 -> holder.imgView.setImageResource(R.drawable.playlist4)
            4 -> holder.imgView.setImageResource(R.drawable.playlist5)
            5 -> holder.imgView.setImageResource(R.drawable.playlist6)
            6 -> holder.imgView.setImageResource(R.drawable.playlist7)
            7 -> holder.imgView.setImageResource(R.drawable.playlist8)
            8 -> holder.imgView.setImageResource(R.drawable.playlist9)
        }

        // 첫 번째 플레이리스트를 선택된 것으로 초기화
        if (position == Constants.DEFAULT_SELECTED_POSITION) {
            selectedImgView = holder.imgView
            holder.imgView.alpha = SELECTED_IMAGE_VIEW_ALPHA
            holder.cardView
        }

        // 플레이리스트 layout 클릭 시 선택 이벤트
        holder.cardLayout.setOnClickListener {
            selectedImgView?.alpha = DEFAULT_IMAGE_VIEW_ALPHA
            selectedImgView = holder.imgView
            holder.imgView.alpha = SELECTED_IMAGE_VIEW_ALPHA
            list[holder.absoluteAdapterPosition].id?.let { it1 -> listener?.selectImg(it1) }
        }

        holder.playListTitle.text = list[position].playListTitle

        // 더보기 클릭하여 나온 menu 에서 item 선택 시 listener 를 통해 기능 수행
        holder.btnMore.setOnClickListener { view ->
            val popupMenu = PopupMenu(applicationContext, view)
            menuInflater.inflate(R.menu.playlist_popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.rename_menu -> { listener?.renamePlayList(holder.absoluteAdapterPosition, list[holder.absoluteAdapterPosition]) }
                    R.id.delete_menu -> { listener?.deletePlayList(list[holder.absoluteAdapterPosition].id!!) }
                }
                false
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface AdapterListener {
        fun selectImg(id: Int)
        fun renamePlayList(pos: Int, playlist: Playlist)
        fun deletePlayList(id: Int)
    }
}