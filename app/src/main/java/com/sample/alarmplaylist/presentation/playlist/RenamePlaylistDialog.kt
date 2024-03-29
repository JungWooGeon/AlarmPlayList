package com.sample.alarmplaylist.presentation.playlist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sample.alarmplaylist.databinding.RenamePlaylistDialogBinding

/**
 * 플레이리스트 이름을 변경할 수 있는 dialog 로 listener 를 통해 PlaylistFragment 로 변경될 이름을 전달
 */
class RenamePlaylistDialog(listener: DialogListener) : DialogFragment() {

    // PlaylistFragment 와 통신할 Listener
    interface DialogListener { fun rename(title: String) }

    private var _binding: RenamePlaylistDialogBinding? = null
    private val binding get() = _binding!!

    private var dialogListener: DialogListener? = listener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = RenamePlaylistDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 취소 버튼 클릭 시 dialog 종료
        binding.btnCancel.setOnClickListener { dismiss() }

        // 저장 버튼 클릭 시 listener 를 통해 이름 변경 기능 수행
        binding.btnConfirm.setOnClickListener {
            dialogListener?.rename(binding.renameEdittext.text.toString())
            dismiss()
        }

        return view
    }
}