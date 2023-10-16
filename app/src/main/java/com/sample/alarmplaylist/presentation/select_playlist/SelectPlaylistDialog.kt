package com.sample.alarmplaylist.presentation.select_playlist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.databinding.SelectPlaylistDialogBinding
import com.sample.alarmplaylist.data.entity.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 알람 추가 화면에서 플레이리스트를 선택할 수 있는 Dialog
 */
class SelectPlaylistDialog(listener: DialogListener) : DialogFragment() {

    // AddAlarmActivity 와 통신하기 위한 Listener
    interface DialogListener {
        // 저장 버튼 클릭 시 플레이리스트 정보를 AddAlarmActivity 에 보내준다.
        fun setPlaylist(playListId: Int, playListTitle: String)
    }

    private var _binding: SelectPlaylistDialogBinding? = null
    private val binding get() = _binding!!

    private var dialogListener: DialogListener? = listener
    private var selectPlaylistRecyclerViewAdapter: SelectPlaylistAdapter? = null

    private val viewModel by viewModel<SelectPlaylistViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SelectPlaylistDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewModel.playlist.observe(viewLifecycleOwner) { playlist -> initRecyclerView(playlist)}
        viewModel.saveAlarm.observe(viewLifecycleOwner) { playlist ->
            if (playlist.isEmpty()) {
                // 선택할 플레이리스트가 없을 경우
                dialogListener?.setPlaylist(Constants.NONE_PLAYLIST_ID, Constants.NONE_PLAYLIST_TITLE)
            } else {
                // 선택할 플레이리스트가 있을 경우
                dialogListener?.setPlaylist(
                    playlist[selectPlaylistRecyclerViewAdapter!!.selectedPosition].id!!,
                    playlist[selectPlaylistRecyclerViewAdapter!!.selectedPosition].playListTitle
                )
            }
        }

        initButton()

        return view
    }

    private fun initButton() {
        // 취소 버튼 클릭 시 dialog 종료
        binding.btnCancel.setOnClickListener { dismiss() }

        // 저장 버튼 클릭 시 선택한 플레이리스트 정보를 listener 를 통하여 activity 에 보내준 후 dialog 종료
        binding.btnConfirm.setOnClickListener {
            viewModel.saveAlarm()

            if (selectPlaylistRecyclerViewAdapter?.list?.isNotEmpty() == true) {
                // 선택할 플레이리스트가 있을 경우
                dialogListener?.setPlaylist(
                    selectPlaylistRecyclerViewAdapter?.list!![selectPlaylistRecyclerViewAdapter!!.selectedPosition].id!!,
                    selectPlaylistRecyclerViewAdapter?.list!![selectPlaylistRecyclerViewAdapter!!.selectedPosition].playListTitle
                )
            } else {
                // 선택할 플레이리스트가 없을 경우
                dialogListener?.setPlaylist(Constants.NONE_PLAYLIST_ID, Constants.NONE_PLAYLIST_TITLE)
            }

            Toast.makeText(requireActivity(), getString(R.string.complete_set_playlist), Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    // 플레이리스트 조회를 완료했을 경우 사용하는 함수로, recyclerview init 수행
    private fun initRecyclerView(playList: List<Playlist>) {
        selectPlaylistRecyclerViewAdapter = SelectPlaylistAdapter()
        selectPlaylistRecyclerViewAdapter!!.list = playList as ArrayList<Playlist>

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
    }
}