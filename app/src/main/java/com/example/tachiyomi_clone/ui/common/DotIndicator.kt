package com.example.tachiyomi_clone.ui.common

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.tachiyomi_clone.R

class DotIndicator(context: Context, private val attrs: AttributeSet) :
    LinearLayout(context, attrs) {

    private var dotSelectedColor = 0
    private var dotsColor = 0
    private var dotSize = 0F
    private var dotCornerRadius = 0F
    private var dotSpacing = 0F

    companion object {
        const val DEFAULT_COLOR = Color.WHITE
    }

    init {
        initAttrSet(attrs)
        initViews()
        setEventListener()
    }

    private fun initAttrSet(attrs: AttributeSet) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.DotIndicator, 0, 0)
        dotSelectedColor = attr.getColor(
            R.styleable.DotIndicator_dotsSelectedColor,
            DEFAULT_COLOR
        )
        dotsColor = attr.getColor(
            R.styleable.DotIndicator_dotsColor,
            DEFAULT_COLOR
        )
        dotSize = attr.getDimension(
            R.styleable.DotIndicator_dotsSize,
            0F
        )
        dotCornerRadius = attr.getDimension(
            R.styleable.DotIndicator_dotsCornerRadius,
            0F
        )
        dotSpacing = attr.getDimension(
            R.styleable.DotIndicator_dotsSpacing,
            0F
        )
        attr.recycle()
    }

    private fun initViews() {
        this.orientation = HORIZONTAL
        this.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    private fun addDots(count: Int? = 0, selectedIndex: Int?) {
        this.removeAllViews()
        for (i in 0 until count!!) {
            this.addView(CircleView(context, this.attrs).apply {
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                params.width = dotSize.toInt()
                params.height = dotSize.toInt()
                if (i != count - 1) {
                    params.setMargins(0, 0, dotSpacing.toInt(), 0)
                }
                layoutParams = params
                setColor(if (selectedIndex == i) dotsColor else dotSelectedColor)
            })
        }
        invalidate()
    }

    private fun setEventListener() {}

    fun attachViewPager2(viewPager: ViewPager2) {
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                addDots(count = viewPager.adapter?.itemCount, selectedIndex = position)
            }
        })
    }
}