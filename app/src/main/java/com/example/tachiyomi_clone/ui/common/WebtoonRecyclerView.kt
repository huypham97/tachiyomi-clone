package com.example.tachiyomi_clone.ui.common

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class WebtoonRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : RecyclerView(context, attrs, defStyle) {

    companion object {
        private const val MIN_RATE = 0.5f
        private const val DEFAULT_RATE = 1f
        private const val MAX_SCALE_RATE = 3f
    }

    private var halfWidth = 0
    private var halfHeight = 0
    private var originalHeight = 0
    private var heightSet = false
    private var currentScale = DEFAULT_RATE

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        halfWidth = MeasureSpec.getSize(widthSpec) / 2
        halfHeight = MeasureSpec.getSize(heightSpec) / 2
        if (!heightSet) {
            originalHeight = MeasureSpec.getSize(heightSpec)
            heightSet = true
        }
        super.onMeasure(widthSpec, heightSpec)
    }

    fun onScale(scaleFactor: Float) {
        currentScale *= scaleFactor
        currentScale = currentScale.coerceIn(
            MIN_RATE,
            MAX_SCALE_RATE,
        )

        setScaleRate(currentScale)

        layoutParams.height = if (currentScale < 1) {
            (originalHeight / currentScale).toInt()
        } else {
            originalHeight
        }
        halfHeight = layoutParams.height / 2
        println("GITBAO originalHeight: $originalHeight halfHeight: $halfHeight")

        if (currentScale != DEFAULT_RATE) {
            x = getPositionX(x)
            y = getPositionY(y)
        } else {
            x = 0f
            y = 0f
        }
        println("GITBAO currentScale: $currentScale x: ${x} y: ${y}")

        requestLayout()
    }

    private fun setScaleRate(rate: Float) {
        scaleX = rate
        scaleY = rate
    }

    private fun getPositionX(positionX: Float): Float {
        if (currentScale < 1) {
            println("GITBAO getPositionX: 0")
            return 0f
        }
        val maxPositionX = halfWidth * (currentScale - 1)
        println("GITBAO getPositionX: ${positionX.coerceIn(-maxPositionX, maxPositionX)}")
        return positionX.coerceIn(-maxPositionX, maxPositionX)
    }

    private fun getPositionY(positionY: Float): Float {
        if (currentScale < 1) {
            println("GITBAO getPositionY: ${(originalHeight / 2 - halfHeight).toFloat()}")
            return (originalHeight / 2 - halfHeight).toFloat()
        }
        val maxPositionY = halfHeight * (currentScale - 1)
        println("GITBAO getPositionY: ${positionY.coerceIn(-maxPositionY, maxPositionY)}")
        return positionY.coerceIn(-maxPositionY, maxPositionY)
    }

}