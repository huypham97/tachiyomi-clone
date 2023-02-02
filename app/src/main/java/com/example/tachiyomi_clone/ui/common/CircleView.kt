package com.example.tachiyomi_clone.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.tachiyomi_clone.R

class CircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        const val DEFAULT_CIRCLE_COLOR = Color.WHITE
        const val DEFAULT_BORDER_COLOR = Color.BLUE
    }

    private lateinit var pain: Paint
    private lateinit var painBorder: Paint
    private var circleColor = 0
    private var borderColor = 0
    private var borderWidth = 0F
    private var circleCenter = 0F
    private var heightCenter = 0
    private var drawnBorder = false
    private var iconDraw: Drawable? = null
    private var marginIcon = 0

    init {
        initViews(context, attrs)
    }

    private fun initViews(context: Context, attrs: AttributeSet) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.CircleView, 0, 0)
        circleColor = attr.getColor(R.styleable.CircleView_cv_color, DEFAULT_CIRCLE_COLOR)
        borderWidth = attr.getDimension(R.styleable.CircleView_cv_border_width, 0F)
        borderColor = attr.getColor(R.styleable.CircleView_cv_border_color, DEFAULT_BORDER_COLOR)
        drawnBorder = attr.getBoolean(R.styleable.CircleView_cv_border, false)
        iconDraw = attr.getDrawable(R.styleable.CircleView_cv_icon)
        attr.recycle()

        pain = Paint().apply {
            this.isAntiAlias = true
            color = circleColor
        }
        painBorder = Paint().apply {
            this.isAntiAlias = true
            color = borderColor
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            if (drawnBorder) {
                drawCircle(circleCenter, circleCenter, circleCenter, painBorder)
            }
            drawCircle(circleCenter, circleCenter, circleCenter - borderWidth, pain)
            iconDraw?.run {
                setBounds(
                    marginIcon,
                    marginIcon,
                    (heightCenter - marginIcon),
                    (heightCenter - marginIcon)
                )
                draw(canvas)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateViews()
    }

    private fun updateViews() {
        heightCenter = Math.min(height, width)
        circleCenter = (heightCenter / 2).toFloat()
        marginIcon = heightCenter * 18 / 100
        invalidate()
    }

    fun setColor(color: Int) {
        this.circleColor = color
        pain.color = color
        invalidate()
    }

    fun setColorBorder(color: Int) {
        this.borderColor = color
        painBorder.color = color
        invalidate()
    }

    fun setDrawnBorder(drawnBorder: Boolean) {
        this.drawnBorder = drawnBorder
        invalidate()
    }

    fun setIconDrawable(iconDrawable: Drawable?) {
        iconDrawable?.let { icon ->
            if (this.iconDraw != icon) {
                this.iconDraw = icon
                invalidate()
            }
        }
    }

    fun setBorderWidth(borderWidth: Float) {
        this.borderWidth = borderWidth
        invalidate()
    }

    fun setIconResources(@DrawableRes imageRes: Int) {
        if (imageRes == 0) {
            this.iconDraw = null
        } else {
            this.iconDraw = ContextCompat.getDrawable(context, imageRes)
        }
        invalidate()
    }
}