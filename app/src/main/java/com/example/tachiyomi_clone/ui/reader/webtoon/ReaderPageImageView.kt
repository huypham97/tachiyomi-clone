package com.example.tachiyomi_clone.ui.reader.webtoon

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.view.isVisible
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.example.tachiyomi_clone.utils.byteArrayToBitmap
import com.example.tachiyomi_clone.utils.system.GLUtil
import java.io.InputStream

class ReaderPageImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttrs: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttrs, defStyleRes) {

    private var pageView: View? = null

    fun recycle() = pageView?.let {
        if (it is SubsamplingScaleImageView) it.recycle()
        it.isVisible = false
    }

    fun setImage(image: Any) {
        prepareNonAnimatedImageView()
        setNonAnimatedImage(image)
    }

    private fun prepareNonAnimatedImageView() {
        if (pageView is SubsamplingScaleImageView) return
        removeView(pageView)

        pageView = WebtoonSubsamplingImageView(context).apply {
            setMaxTileSize(GLUtil.maxTextureSize)
            setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
            setPanLimit(SubsamplingScaleImageView.PAN_LIMIT_INSIDE)
            setMinimumTileDpi(180)
        }
        addView(pageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun setNonAnimatedImage(
        image: Any
    ) = (pageView as? SubsamplingScaleImageView)?.apply {
        setMinimumDpi(1)

        when (image) {
            is ByteArray -> {
                val bitmap = byteArrayToBitmap(image)
                bitmap?.let {
                    setImage(ImageSource.bitmap(it))
                }
            }
            is Drawable -> {
                val bitmap = (image as BitmapDrawable).bitmap
                setImage(ImageSource.bitmap(bitmap))
            }
            is InputStream -> setImage(ImageSource.inputStream(image))
            else -> throw IllegalArgumentException("Not implemented for class ${image::class.simpleName}")
        }
        isVisible = true
    }

}