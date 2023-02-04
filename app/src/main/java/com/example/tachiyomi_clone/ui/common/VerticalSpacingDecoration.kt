package com.example.tachiyomi_clone.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class VerticalSpacingDecoration(
    context: Context,
    resId: Int,
    private val isEdging: Boolean?
) :
    RecyclerView.ItemDecoration() {

    private var divider: Drawable? = null

    init {
        divider = ContextCompat.getDrawable(context, resId)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//        val left: Int = parent.paddingLeft
//        val right: Int =
//            parent.width - parent.paddingRight
//
//        val childCount: Int =
//            parent.childCount
//        for (i in 0 until childCount) {
//            val child: View = parent.getChildAt(i)
//            val params = child.layoutParams as RecyclerView.LayoutParams
//
//            val top = child.bottom + params.bottomMargin
//            val bottom = top + divider!!.intrinsicHeight
//            divider!!.setBounds(left, top, right, bottom)
//            divider!!.draw(c)
//        }
        val childCount: Int =
            parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            if (parent.getChildAdapterPosition(
                    child
                ) == 0
            ) {
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.top - params.topMargin
                val bottom = top + divider!!.intrinsicHeight
                val left: Int =
                    parent.paddingLeft
                val right: Int =
                    parent.width - parent.paddingRight
                divider!!.setBounds(left, top, right, bottom)
                divider!!.draw(c)
            }
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider!!.intrinsicHeight
            val left: Int =
                parent.paddingLeft
            val right: Int =
                parent.width - parent.paddingRight
            divider!!.setBounds(left, top, right, bottom)
            divider!!.draw(c)
        }
    }
}