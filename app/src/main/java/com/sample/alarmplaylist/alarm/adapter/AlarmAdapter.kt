package com.sample.alarmplaylist.alarm.adapter

import android.view.*
import android.view.View.OnCreateContextMenuListener
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sample.alarmplaylist.R

class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v:View, pos : Int)
    }
    var listener : OnItemClickListener? = null

    val list = ArrayList<String>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnCreateContextMenuListener {
        var cardLayout: ConstraintLayout = itemView.findViewById((R.id.card_layout))
        var itemAlarmTime: TextView = itemView.findViewById(R.id.alarm_time)
        var itemAlarmPlaylist: TextView = itemView.findViewById(R.id.playlist_name)
        var itemSwitchToggle: SwitchCompat = itemView.findViewById(R.id.alarm_switch)

        init {
            cardLayout.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(bindingAdapterPosition, 121, 0, "삭제")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_layout, parent, false)

        return MyViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemSwitchToggle
        holder.itemAlarmTime.setText(list[position])
        holder.itemAlarmPlaylist.setText("플레이리스트 $position")

        var colorId = 0
        when(position % 4) {
            0 -> colorId = R.color.card_1
            1 -> colorId = R.color.card_2
            2 -> colorId = R.color.card_3
            3 -> colorId = R.color.card_4
        }
        holder.cardLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, colorId))

        holder.cardLayout.setOnClickListener {
            listener?.onItemClick(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}