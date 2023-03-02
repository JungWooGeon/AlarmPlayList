package com.sample.alarmplaylist.playlist

import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.alarmplaylist.databinding.FragmentPlaylistBinding
import com.sample.alarmplaylist.playlist.adapter.MusicListAdapter
import com.sample.alarmplaylist.playlist.adapter.PlayListAdapter
import com.sample.alarmplaylist.playlist.add_playlist.AddPlaylistActivity
import com.sample.alarmplaylist.playlist.playlist_db.PlayList

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var ct: ViewGroup? = null

    private lateinit var viewModel: PlaylistViewModel

    private lateinit var playListRecyclerViewAdapter: PlayListAdapter
    private lateinit var musicListRecyclerViewAdapter: MusicListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ct = container
        viewModel = ViewModelProvider(this)[PlaylistViewModel::class.java]

        viewModel.playList.observe(viewLifecycleOwner) { list -> initRecyclerView(list) }

        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initButton()

        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel.readPlayList(requireActivity().applicationContext)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initButton() {
        binding.btnAddList.setOnClickListener {
            viewModel.addPlayList(requireActivity().applicationContext)
        }

        binding.btnAddMusic.setOnClickListener {
            startActivity(Intent(requireActivity(), AddPlaylistActivity::class.java))
        }
    }

    private fun initRecyclerView(playList: List<PlayList>) {
        playListRecyclerViewAdapter = PlayListAdapter(requireActivity().applicationContext, requireActivity().menuInflater)
        playListRecyclerViewAdapter.listener = (object : PlayListAdapter.AdapterListener {
            override fun renamePlayList(pos: Int) {
                val dialog = RenamePlayListDialog((object: RenamePlayListDialog.DialogListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun rename(title: String) {
                        viewModel.renamePlayList(requireActivity().applicationContext, pos, title)
                        playListRecyclerViewAdapter.list[pos].playListTitle = title
                        playListRecyclerViewAdapter.notifyDataSetChanged()
                        Toast.makeText(activity, "제목이 $title(으)로 변경되었습니다", Toast.LENGTH_SHORT).show()
                    }
                }))
                // 알림창이 띄워져있는 동안 배경 클릭 막기
                dialog.isCancelable = false
                dialog.show(activity?.supportFragmentManager!!, "RenamePlayListDialog")
            }

            override fun deletePlayList(pos: Int) {
                viewModel.deletePlayList(requireActivity().applicationContext, pos)
                Toast.makeText(activity, "플레이리스트가 삭제되었습니다", Toast.LENGTH_SHORT).show()
            }
        })

        playListRecyclerViewAdapter.list = playList as ArrayList<PlayList>
        binding.playlist.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = playListRecyclerViewAdapter
        }

        if (playListRecyclerViewAdapter.list.size == 0) {
            binding.playlistEmptyView.visibility = View.VISIBLE
        } else {
            binding.playlistEmptyView.visibility = View.GONE
        }

//
//        musicListRecyclerViewAdapter = MusicListAdapter()
//        binding.musicList.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            adapter = musicListRecyclerViewAdapter
//        }

//        if (musicListRecyclerViewAdapter.list.size == 0) {
//            binding.musicListEmptyView.visibility = View.VISIBLE
//        } else {
//            binding.musicListEmptyView.visibility = View.GONE
//        }
    }
}