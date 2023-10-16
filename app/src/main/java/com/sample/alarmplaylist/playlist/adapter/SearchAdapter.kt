package com.sample.alarmplaylist.playlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.playlist.youtube_db.Youtube

/**
 * AddPlaylistActivity 에서 사용하는 검색 결과 recyclerview adapter
 */
class SearchAdapter(private val lifecycle: Lifecycle) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    // AddPlayListActivity 와 통신하기 위한 listener
    interface AdapterListener { fun onAddButtonClick(pos: Int) }

    var listener: AdapterListener? = null
    var list = ArrayList<Youtube>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var youtubePlayerView: YouTubePlayerView = itemView.findViewById(R.id.youtube_player_view)
        var addPlayList: AppCompatImageView = itemView.findViewById(R.id.btn_add_playlist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_youtube_search_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // youtubePlayerView 설정, (cueVideo : 썸네일 o 자동재생 x, loadVideo : 썸네일 x 자동재생 o)
        lifecycle.addObserver(holder.youtubePlayerView)
        holder.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = list[holder.bindingAdapterPosition].videoId
                youTubePlayer.cueVideo(videoId, Constants.YOUTUBE_PLAYER_VIEW_START_SECONDS)
            }
        })

        holder.title.text = list[position].title
        holder.addPlayList.setOnClickListener { listener?.onAddButtonClick(position) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onViewRecycled(holder: MyViewHolder) {
        holder.youtubePlayerView.release()
        super.onViewRecycled(holder)
    }
}