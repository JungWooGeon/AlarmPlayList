package com.sample.alarmplaylist.playlist.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sample.alarmplaylist.databinding.RenamePlaylistDialogBinding

class RenamePlayListDialog(listener: DialogListener) : DialogFragment() {

    interface DialogListener {
        fun rename(title: String)
    }

    private var _binding: RenamePlaylistDialogBinding? = null
    private val binding get() = _binding!!

    private var dialogListener: DialogListener? = listener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RenamePlaylistDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            dialogListener?.rename(binding.renameEdittext.text.toString())
            dismiss()
        }

        return view
    }
}