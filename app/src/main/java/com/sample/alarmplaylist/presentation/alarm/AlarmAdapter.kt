package com.sample.alarmplaylist.presentation.alarm

import android.content.Context
import android.view.*
import android.view.View.OnCreateContextMenuListener
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.data.entity.Alarm

/**
 * AlarmFragment 에 알람 정보들을 보여주는 RecyclerView 에서 사용하는 Adapter
 */
class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.MyViewHolder>() {

    companion object {
        private const val CONTEXT_MENU_ITEM_ID = 121
        private const val CONTEXT_MENU_ORDER = 0
        private const val CONTEXT_MENU_TITLE = "삭제"
        private const val ALARM_BACKGROUND_COLOR_COUNT = 4

        private const val ALARM_TWELVE = 12
        private const val ALARM_TEN = 10
        private const val ALARM_ZERO = 0
    }

    var listener: AdapterListener? = null

    var alarmList = ArrayList<Alarm>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnCreateContextMenuListener {
        var cardLayout: ConstraintLayout = itemView.findViewById((R.id.card_layout))
        var itemAlarmTime: TextView = itemView.findViewById(R.id.alarm_time)
        var itemAlarmPlaylist: TextView = itemView.findViewById(R.id.playlist_name)
        var itemSwitchToggle: SwitchCompat = itemView.findViewById(R.id.alarm_switch)

        // init ContextMenu
        init { cardLayout.setOnCreateContextMenuListener(this) }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.add(bindingAdapterPosition, CONTEXT_MENU_ITEM_ID, CONTEXT_MENU_ORDER, CONTEXT_MENU_TITLE)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_alarm_layout, parent, false)

        return MyViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // bind view
        holder.itemSwitchToggle.isChecked = alarmList[position].onOff
        holder.itemAlarmTime.text = getAlarmTimeToString(holder.itemView.context, alarmList[position])
        holder.itemAlarmPlaylist.text = alarmList[position].playlistName

        // position 에 따라 배경색을 다르게 주기
        var colorId = 0
        when(position % ALARM_BACKGROUND_COLOR_COUNT) {
            0 -> colorId = R.color.card_1
            1 -> colorId = R.color.card_2
            2 -> colorId = R.color.card_3
            3 -> colorId = R.color.card_4
        }
        holder.cardLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, colorId))

        // 알람 layout 클릭 시 listener 를 통하여 AlarmFragment 에 이벤트 전달
        holder.cardLayout.setOnClickListener { listener?.onItemClick(alarmList[holder.absoluteAdapterPosition]) }

        // 알람 switch 클릭 시 listener 를 통하여 AlarmFragment 에 이벤트 전달
        holder.itemSwitchToggle.setOnCheckedChangeListener { _, isChecked ->
            listener?.onCheckedChange(isChecked, alarmList[holder.absoluteAdapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    private fun getAlarmTimeToString(context: Context, it: Alarm): String {
        var result = if (it.alarmHour.toInt() >= ALARM_TWELVE) { context.getString(R.string.pm) } else { context.getString(R.string.am) }
        result += if (it.alarmHour.toInt() == ALARM_ZERO || it.alarmHour.toInt() == ALARM_TWELVE) {
            context.getString(R.string.twelve)
        } else if ((it.alarmHour.toInt() % ALARM_TWELVE) < ALARM_TEN) {
            context.getString(R.string.zero) + (it.alarmHour.toInt() % ALARM_TWELVE).toString()
        } else {
            (it.alarmHour.toInt() % ALARM_TWELVE).toString()
        }
        result += if (it.alarmMinute.toInt() < ALARM_TEN) {
            context.getString(R.string.colon) + context.getString(R.string.zero) + it.alarmMinute
        } else {
            context.getString(R.string.colon) + it.alarmMinute
        }

        return result
    }

    // AlarmFragment 와 통신할 Listener
    interface AdapterListener {
        fun onItemClick(alarm: Alarm)
        fun onCheckedChange(isChecked: Boolean, alarm: Alarm)
    }
}