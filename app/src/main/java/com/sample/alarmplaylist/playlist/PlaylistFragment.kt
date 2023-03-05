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
import com.sample.alarmplaylist.Constant
import com.sample.alarmplaylist.R
import com.sample.alarmplaylist.databinding.FragmentPlaylistBinding
import com.sample.alarmplaylist.playlist.adapter.MusicListAdapter
import com.sample.alarmplaylist.playlist.adapter.PlayListAdapter
import com.sample.alarmplaylist.playlist.add_playlist.AddPlaylistActivity
import com.sample.alarmplaylist.playlist.dialog.RenamePlayListDialog
import com.sample.alarmplaylist.playlist.playlist_db.PlayList
import com.sample.alarmplaylist.playlist.youtube_db.Youtube

/**
 * 플레이리스트 정보를 보여주는 화면이고, 플레이리스트를 추가하거나 AddPlaylistActivity 를 띄워
 * 플레이리스트에 음악목록을 추가할 수 있다.
 */
class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlaylistViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this)[PlaylistViewModel::class.java]

        // 플레이리스트 혹은 음악 정보가 변경 될 때, recyclerview update
        viewModel.playList.observe(viewLifecycleOwner) { list -> initPlayList(list) }
        viewModel.musicList.observe(viewLifecycleOwner) { list -> initMusicList(list) }

        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initButton()

        return root
    }

    override fun onResume() {
        super.onResume()
        // 음악이 추가되고 다시 이 activity 로 전환되었을 시 플레이리스트 목록 update 를 통해 화면 재구성
        viewModel.readPlayList(requireActivity().applicationContext)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initButton() {
        // 플레이리스트 추가 버튼을 눌렀을 경우, DB 에 플레이리스트를 추가
        binding.btnAddList.setOnClickListener { viewModel.addPlayList(requireActivity().applicationContext) }

        // 음악 추가 버튼을 눌렀을 경우, AddPlaylistActivity 를 띄운다.
        binding.btnAddMusic.setOnClickListener {
            val intent = Intent(requireActivity(), AddPlaylistActivity::class.java)
            intent.putExtra(Constant.PLAYLIST_ID, viewModel.getSelectPlaylistID())
            startActivity(intent)
        }
    }

    // init playlist recyclerview
    private fun initPlayList(playList: List<PlayList>) {
        val playListRecyclerViewAdapter = PlayListAdapter(requireActivity().applicationContext, requireActivity().menuInflater)
        // PlaylistFragment 와 adapter 사이에 통신하기 위한 listener
        playListRecyclerViewAdapter.listener = (object : PlayListAdapter.AdapterListener {
            // 이미지 선택 시, 선택한 이미지에 해당하는 정보를 조회하여 Model 에 반영
            override fun selectImg(pos: Int) { viewModel.selectImg(requireActivity(), pos) }

            // 플레이리스트의 '더보기' 메뉴에서 '이름 변경'을 클릭하였을 경우 RenamePlayListDialog 를 띄움
            override fun renamePlayList(pos: Int) {
                val dialog = RenamePlayListDialog((object: RenamePlayListDialog.DialogListener {
                    // PlaylistFragment 와 dialog 사이에 통신하기 위한 listener
                    @SuppressLint("NotifyDataSetChanged")
                    override fun rename(title: String) {
                        // 이름을 정상적으로 변경하면 DB 에 반영 후, recyclerview 새로고침
                        viewModel.renamePlayList(requireActivity().applicationContext, pos, title)
                        playListRecyclerViewAdapter.list[pos].playListTitle = title
                        playListRecyclerViewAdapter.notifyDataSetChanged()
                        Toast.makeText(activity, getString(R.string.title) + title + getString(R.string.update), Toast.LENGTH_SHORT).show()
                    }
                }))
                // 알림창이 띄워져있는 동안 배경 클릭 막기
                dialog.isCancelable = false
                dialog.show(activity?.supportFragmentManager!!, getString(R.string.rename_playlist_dialog))
            }

            // 플레이리스트의 '더보기' 메뉴에서 '삭제'를 클릭하였을 경우 해당 플레이리스트 삭제
            override fun deletePlayList(pos: Int) {
                viewModel.deletePlayList(requireActivity().applicationContext, pos)
                Toast.makeText(activity, getString(R.string.delete_playlist), Toast.LENGTH_SHORT).show()
            }
        })

        // DB 에서 변경된 정보(list)를 adapter 에 반영
        playListRecyclerViewAdapter.list = playList as ArrayList<PlayList>
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
    private fun initMusicList(musicList: List<Youtube>) {
        val musicListRecyclerViewAdapter = MusicListAdapter(requireActivity(), requireActivity().menuInflater)
        // PlaylistFragment 와 adapter 사이에 통신하기 위한 listener
        musicListRecyclerViewAdapter.listener = (object : MusicListAdapter.AdapterListener {
            // 음악 목록의 '더보기' 메뉴에서 '삭제'를 클릭하였을 경우 해당 음악 삭제
            override fun deleteMusic(pos: Int) {
                viewModel.deleteMusic(requireActivity(), pos)
                Toast.makeText(activity, getString(R.string.delete_music), Toast.LENGTH_SHORT).show()
            }
        })

        // DB 에서 변경된 정보(list)를 adapter 에 반영
        musicListRecyclerViewAdapter.list = musicList as ArrayList<Youtube>
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