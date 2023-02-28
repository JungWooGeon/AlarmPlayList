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
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.playlist.youtube_db.Youtube

/**
 * https://github.com/PierfrancescoSoffritti/android-youtube-player#autoPlay
 */

class SearchAdapter(private val lifecycle: Lifecycle) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    interface AdapterListener { fun onAddButtonClick(pos: Int) }

    var listener: AdapterListener? = null
    var list = ArrayList<Youtube>()
    var youtubePlayerList = ArrayList<YouTubePlayer>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var youtubePlayerView: YouTubePlayerView = itemView.findViewById(R.id.youtube_player_view)
        var addPlayList: AppCompatImageView = itemView.findViewById(R.id.btn_add_playlist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.youtube_search_result_layout, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        lifecycle.addObserver(holder.youtubePlayerView)

        holder.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = list[holder.bindingAdapterPosition].videoId
                youTubePlayer.cueVideo(videoId, 0F)
            }
        })

        holder.title.text = list[position].title
        holder.addPlayList.setOnClickListener { listener?.onAddButtonClick(position) }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}