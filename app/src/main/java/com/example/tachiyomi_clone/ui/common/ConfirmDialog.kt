package com.example.tachiyomi_clone.ui.common

import androidx.fragment.app.FragmentManager
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.widget.BottomSheetDialog
import com.example.tachiyomi_clone.databinding.DialogConfirmBinding

class ConfirmDialog(private val onClickConfirmListener: (() -> Unit)?) :
    BottomSheetDialog<DialogConfirmBinding>() {
    override fun getLayoutResId(): Int = R.layout.dialog_confirm

    companion object {
        private fun newInstance(onClickConfirmListener: (() -> Unit)?) =
            ConfirmDialog(onClickConfirmListener)

        @JvmStatic
        fun show(
            fragmentManager: FragmentManager,
            onClickConfirmListener: (() -> Unit)? = null
        ) {
            val confirmDialog = newInstance(onClickConfirmListener)
            confirmDialog.show(fragmentManager, confirmDialog.tag)
        }
    }

    override fun setEventListener() {
        super.setEventListener()
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnOk.setOnClickListener {
            onClickConfirmListener?.invoke()
            dismiss()
        }
    }
}