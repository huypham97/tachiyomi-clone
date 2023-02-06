package com.example.tachiyomi_clone.common.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.tachiyomi_clone.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BottomSheetDialog<B : ViewDataBinding>(private val themeId: Int = R.style.BottomSheetDialogStyle) :
    BottomSheetDialogFragment(), LifecycleObserver {

    protected lateinit var binding: B

    open val isFullScreen: Boolean = false

    open val isDragging: Boolean = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (!isFullScreen) {
            val dialog = super.onCreateDialog(savedInstanceState)
            dialog.setOnShowListener {
                val bottomSheetDialog = it as BottomSheetDialog
                val parentLayout =
                    bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                        ?: return@setOnShowListener
                parentLayout.setBackgroundColor(Color.TRANSPARENT)
                if (isDragging) {
                    parentLayout.let { bottomSheet ->
                        val behaviour = BottomSheetBehavior.from(parentLayout)
                        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
                        bottomSheetDialog.behavior.addBottomSheetCallback(object :
                            BottomSheetBehavior.BottomSheetCallback() {
                            override fun onStateChanged(bottomSheet: View, newState: Int) {
                                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                    bottomSheetDialog.behavior.state =
                                        BottomSheetBehavior.STATE_EXPANDED
                                }
                            }

                            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                            }

                        })
                    }
                }
            }
            configDialog(dialog)
            return dialog
        } else {
            val dialog = BottomSheetDialog(requireContext(), theme)
            dialog.apply {
                val window = dialog.window
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setOnShowListener {
                    val bottomSheetDialog = it as BottomSheetDialog
                    val parentLayout =
                        bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                    parentLayout?.let { bottomSheet ->
                        val behaviour = BottomSheetBehavior.from(parentLayout)
                        setupFullHeight(parentLayout)
                        behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
            configDialog(dialog)
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, themeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setContentView(inflater, getLayoutResId(), container)
        return binding.root
    }

    override fun onAttach(context: Context) {
        lifecycle.addObserver(this)
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        activity?.lifecycle?.removeObserver(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setEventListener()
    }

    protected open fun setContentView(
        inflater: LayoutInflater,
        layoutResID: Int,
        container: ViewGroup?
    ) {
        binding = DataBindingUtil.inflate(inflater, layoutResID, container, false)
    }

    fun showDialog(fragmentManager: FragmentManager?, tag: String? = this.tag) {
        fragmentManager?.let { show(it, tag) }
    }

    protected abstract fun getLayoutResId(): Int

    protected open fun initViews() {}

    protected open fun setEventListener() {}

    open fun screen(): DialogScreen = DialogScreen()

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun configDialog(dialog: Dialog) {
        dialog.setCanceledOnTouchOutside(screen().isDismissByTouchOutSide)
    }
}

class DialogScreen(
    var mode: DIALOG_MODE = DIALOG_MODE.NORMAL,
    var isFullWidth: Boolean = false,
    var isFullHeight: Boolean = false,
    var isDismissByTouchOutSide: Boolean = true,
    var isDismissByOnBackPressed: Boolean = true
) {
    enum class DIALOG_MODE {
        NORMAL,
        BOTTOM
    }
}