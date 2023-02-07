package com.example.tachiyomi_clone.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.ViewCustomEditTextBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity
import com.example.tachiyomi_clone.utils.system.KeyboardUtility
import com.google.android.material.textfield.TextInputEditText

class CustomEditText @JvmOverloads constructor(
    private val ctx: Context, attrs: AttributeSet? = null
) : FrameLayout(ctx, attrs) {

    companion object {
        const val TAG = "ETEditTextView"
    }

    private lateinit var binding: ViewCustomEditTextBinding
    private var hint: String = ""
    private var inputType: Int = 0
        set(value) {
            field = value
            binding.etEditTextViewInput.inputType = value
        }
    var editTextWatcherListener: EditTextWatcher? = null
    private var maxLines: Int? = null
        set(value) {
            field = value
            if (value != null) {
                binding.etEditTextViewInput.maxLines = value
            }
        }
    private var minLines: Int = 0
        set(value) {
            field = value
            binding.etEditTextViewInput.minLines = field
        }
    private var imeOptions: Int? = null
        set(value) {
            field = value
            if (value != null && value != -1) {
                binding.etEditTextViewInput.imeOptions = value
                if (inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE) binding.etEditTextViewInput.setRawInputType(
                    InputType.TYPE_CLASS_TEXT
                )
            }
        }

    init {
        initBinding()
        initAttrSet(attrs)
        initViews()
        setEventListeners()
    }

    private fun initBinding() {
        binding = ViewCustomEditTextBinding.inflate(LayoutInflater.from(context), this, true)
        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    private fun initAttrSet(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText)
        maxLines = a.getInt(R.styleable.CustomEditText_edt_max_lines, Int.MAX_VALUE)
        minLines = a.getInt(R.styleable.CustomEditText_edt_min_lines, 1)
        hint = a.getString(R.styleable.CustomEditText_edt_hint) ?: ""
        inputType = a.getInt(R.styleable.CustomEditText_edt_input_type, InputType.TYPE_CLASS_TEXT)
        imeOptions = a.getInt(R.styleable.CustomEditText_edt_ime_options, -1)
    }

    private fun initViews() {
        binding.etEditTextViewInput.hint = this.hint
        binding.tilEtEditTextViewInput.isHintEnabled = false
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setEventListeners() {
        this.binding.etEditTextViewInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                editTextWatcherListener?.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextWatcherListener?.onTextChange(s, start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                if (s == null || s.isBlank()) {
                    editTextWatcherListener?.afterTextChanged(s.toString())
                } else {
                    editTextWatcherListener?.afterTextChanged(s.toString())
                }
            }
        })

        binding.etEditTextViewInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                this.binding.tilEtEditTextViewInput.clearFocus()
                KeyboardUtility.hideSoftKeyboard(ctx as BaseActivity<*, *>)
            }
            return@setOnEditorActionListener false
        }
        binding.etEditTextViewInput.setOnTouchListener { v, event ->
            if (v.id == binding.etEditTextViewInput.id) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            return@setOnTouchListener false
        }

    }

    fun getTextInputEditText(): TextInputEditText = binding.etEditTextViewInput
}

interface EditTextWatcher {
    fun onTextChange(s: CharSequence?, start: Int, before: Int, count: Int) {}
    fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    fun afterTextChanged(s: String?) {}
}
