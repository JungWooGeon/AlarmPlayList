package com.sample.alarmplaylist.playlist

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        playListRecyclerViewAdapter = PlayListAdapter()
        playListRecyclerViewAdapter.list = playList as ArrayList<PlayList>
        binding.playlist.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = playListRecyclerViewAdapter
        }
//
//        musicListRecyclerViewAdapter = MusicListAdapter()
//        binding.musicList.apply {
//            setHasFixedSize(true)
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            adapter = musicListRecyclerViewAdapter
//        }
    }
}