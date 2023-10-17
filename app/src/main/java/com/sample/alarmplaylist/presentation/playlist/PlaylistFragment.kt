package com.sample.alarmplaylist.presentation.playlist

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.alarmplaylist.Constants
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.data.entity.Playlist
import com.sample.alarmplaylist.data.entity.Youtube
import com.sample.alarmplaylist.databinding.FragmentPlaylistBinding
import com.sample.alarmplaylist.presentation.shared_adapters.MusicListAdapter
import com.sample.alarmplaylist.presentation.add_playlist.AddPlaylistActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * 플레이리스트 정보를 보여주는 화면이고, 플레이리스트를 추가하거나 AddPlaylistActivity 를 띄워
 * 플레이리스트에 음악목록을 추가할 수 있다.
 */
class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistViewModel>()

    private var selectedPlaylistId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // 플레이리스트 혹은 음악 정보가 변경 될 때, recyclerview update
        viewModel.playLists.observe(viewLifecycleOwner) { playlists ->
            if (playlists.isNotEmpty()) {
                selectedPlaylistId = playlists[0].id!!
            }
            initPlaylists(playlists)
        }
        viewModel.youtubes.observe(viewLifecycleOwner) { youtubes -> initYoutubes(youtubes) }

        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initButton()

        return root
    }

    override fun onResume() {
        super.onResume()
        // 음악이 추가되고 다시 이 activity 로 전환되었을 시 플레이리스트 목록 update 를 통해 화면 재구성
        viewModel.loadPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initButton() {
        // 플레이리스트 추가 버튼을 눌렀을 경우, DB 에 플레이리스트를 추가
        binding.btnAddList.setOnClickListener { viewModel.addPlayList(getString(R.string.playlist)) }

        // 음악 추가 버튼을 눌렀을 경우, AddPlaylistActivity 를 띄운다.
        binding.btnAddMusic.setOnClickListener {
            if (viewModel.playlistIsEmpty()) {
                Toast.makeText(activity, getString(R.string.plz_add_first_playlist), Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(requireActivity(), AddPlaylistActivity::class.java)
                intent.putExtra(Constants.PLAYLIST_ID, selectedPlaylistId)
                startActivity(intent)
            }
        }
    }

    // init playlist recyclerview
    private fun initPlaylists(playList: List<Playlist>) {
        val playListRecyclerViewAdapter = PlaylistAdapter(requireActivity().applicationContext, requireActivity().menuInflater)

        playListRecyclerViewAdapter.listener = (object : PlaylistAdapter.AdapterListener {
            // 이미지 선택 시, 선택한 이미지에 해당하는 정보를 조회하여 Model 에 반영
            override fun selectImg(id: Int) {
                selectedPlaylistId = id
                viewModel.getSelectedYoutubesById(id)
            }

            // 플레이리스트의 '더보기' 메뉴에서 '이름 변경'을 클릭하였을 경우 RenamePlayListDialog 를 띄움
            override fun renamePlayList(pos: Int, playlist: Playlist) {
                val dialog = RenamePlaylistDialog((object: RenamePlaylistDialog.DialogListener {
                    override fun rename(title: String) {
                        // 이름을 정상적으로 변경하면 DB 에 반영 후, recyclerview 새로고침
                        playlist.playListTitle = title
                        viewModel.updatePlaylist(playlist)

                        Toast.makeText(activity, getString(R.string.title) + playlist.playListTitle + getString(R.string.update), Toast.LENGTH_SHORT).show()

                        playListRecyclerViewAdapter.list[pos].playListTitle = title
                        playListRecyclerViewAdapter.notifyItemChanged(pos)
                    }
                }))
                // 알림창이 띄워져있는 동안 배경 클릭 막기
                dialog.isCancelable = false
                dialog.show(activity?.supportFragmentManager!!, getString(R.string.rename_playlist_dialog))
            }

            // 플레이리스트의 '더보기' 메뉴에서 '삭제'를 클릭하였을 경우 해당 플레이리스트 삭제
            override fun deletePlayList(id: Int) {
                viewModel.deletePlayList(id)
                Toast.makeText(activity, getString(R.string.delete_playlist), Toast.LENGTH_SHORT).show()
            }
        })

        // DB 에서 변경된 정보(list)를 adapter 에 반영
        playListRecyclerViewAdapter.list = playList as ArrayList<Playlist>
        binding.playlist.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = playListRecyclerViewAdapter
        }

        // recyclerview empty view 설정
        if (playListRecyclerViewAdapter.list.isEmpty()) {
            binding.playlistEmptyView.visibility = View.VISIBLE
        } else {
            binding.playlistEmptyView.visibility = View.GONE
        }
    }

    // init musicList recyclerview
    private fun initYoutubes(youtubes: List<Youtube>) {
        val musicListRecyclerViewAdapter = MusicListAdapter(requireActivity(), requireActivity().menuInflater)
        musicListRecyclerViewAdapter.listener = (object : MusicListAdapter.AdapterListener {
            // 음악 목록의 '더보기' 메뉴에서 '삭제'를 클릭하였을 경우 해당 음악 삭제
            override fun deleteMusic(id: Int) {
                viewModel.deleteYoutubeById(id, selectedPlaylistId)
                Toast.makeText(activity, getString(R.string.delete_music), Toast.LENGTH_SHORT).show()
            }
        })

        // DB 에서 변경된 정보(list)를 adapter 에 반영
        musicListRecyclerViewAdapter.list = youtubes.toCollection(ArrayList())
        binding.musicList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = musicListRecyclerViewAdapter
        }

        // recyclerview empty view 설정
        if (musicListRecyclerViewAdapter.list.isEmpty()) {
            binding.musicListEmptyView.visibility = View.VISIBLE
        } else {
            binding.musicListEmptyView.visibility = View.GONE
        }
    }
}