package com.example.tachiyomi_clone.ui.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.doOnTextChanged
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.ExpandableTextViewBinding

class ExpandableTextView constructor(
    ctx: Context,
    attrs: AttributeSet? = null
) : LinearLayout(ctx, attrs) {

    private lateinit var binding: ExpandableTextViewBinding
    private var mMaxLines = Int.MAX_VALUE
    private var backgroundContainer: Drawable? = null
    private var expandable = false

    init {
        initAttrSet(attrs)
        initViews()
        setEvenListeners()
    }

    private fun initAttrSet(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        mMaxLines = a.getInt(R.styleable.ExpandableTextView_etv_max_line, Int.MAX_VALUE)
        backgroundContainer =
            a.getDrawable(R.styleable.ExpandableTextView_etv_background_resource)
    }

    private fun initViews() {
        this.binding = ExpandableTextViewBinding.inflate(LayoutInflater.from(context), this, true)
        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        binding.eivExpandableSeeMore.setState(ExpandIconView.MORE, true)
        backgroundContainer?.let {
            binding.root.background = backgroundContainer
        }
    }

    private fun setEvenListeners() {
        binding.eivExpandableSeeMore.setOnClickListener {
            if (binding.tvExpandableNote.maxLines == Int.MAX_VALUE) {
                binding.tvExpandableNote.maxLines = mMaxLines
                expandable = false
                binding.eivExpandableSeeMore.setState(ExpandIconView.MORE, true)
            } else {
                binding.tvExpandableNote.maxLines = Int.MAX_VALUE
                expandable = true
                binding.eivExpandableSeeMore.setState(ExpandIconView.LESS, true)
            }
        }

        binding.tvExpandableNote.apply {
            doOnTextChanged { _, _, _, _ ->
                post {
                    if (lineCount > mMaxLines) {
                        binding.eivExpandableSeeMore.visibility = View.VISIBLE
                        expandable = true
                    } else {
                        binding.eivExpandableSeeMore.visibility = View.GONE
                        expandable = false
                    }
                    maxLines = mMaxLines
                    requestLayout()
                }
            }
        }
    }

    fun setNoteContent(text: Spanned?) {
        binding.tvExpandableNote.text = text
    }

    fun setNoteContent(text: CharSequence?) {
        binding.tvExpandableNote.text = text
    }

    fun setNoteContent(text: String?) {
        binding.tvExpandableNote.text = text
    }
}
