package com.sample.alarmplaylist.alarm.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.sample.alarmplaylist.alarm.adapter.SelectPlaylistAdapter
import com.sample.alarmplaylist.databinding.SelectPlaylistDialogBinding
import com.sample.alarmplaylist.playlist.playlist_db.PlayList
import com.sample.alarmplaylist.playlist.playlist_db.PlayListDataBase

class SelectPlaylistDialog(listener: DialogListener) : DialogFragment() {

    interface DialogListener {
        fun setPlaylist(playListId: Int, playListTitle: String)
    }

    private var _binding: SelectPlaylistDialogBinding? = null
    private val binding get() = _binding!!

    private var dialogListener: DialogListener? = listener
    private var selectPlaylistRecyclerViewAdapter: SelectPlaylistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SelectPlaylistDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            // id가 -1 일 경우 없는 것
            if (selectPlaylistRecyclerViewAdapter?.list?.size != 0) {
                dialogListener?.setPlaylist(
                    selectPlaylistRecyclerViewAdapter?.list!![selectPlaylistRecyclerViewAdapter!!.selectedPosition].id!!,
                    selectPlaylistRecyclerViewAdapter?.list!![selectPlaylistRecyclerViewAdapter!!.selectedPosition].playListTitle
                )
            } else {
                dialogListener?.setPlaylist(-1, "")
            }

            Toast.makeText(requireActivity(), "플레이리스트가 설정되었습니다", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        selectPlaylistRecyclerViewAdapter = SelectPlaylistAdapter()

        var playList: List<PlayList>? = null
        val db : PlayListDataBase = Room.databaseBuilder(requireContext(), PlayListDataBase::class.java,
            "playListDB"
        ).build()

        val thread = Thread {
            playList = db.playListDao().getAll()
        }

        thread.start()
        thread.join()

        selectPlaylistRecyclerViewAdapter!!.list = playList as ArrayList<PlayList>

        binding.playlist.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = selectPlaylistRecyclerViewAdapter
        }

        if (selectPlaylistRecyclerViewAdapter!!.list.size == 0) {
            binding.playlistEmptyView.visibility = View.VISIBLE
        } else {
            binding.playlistEmptyView.visibility = View.GONE
        }

        return view
    }
}