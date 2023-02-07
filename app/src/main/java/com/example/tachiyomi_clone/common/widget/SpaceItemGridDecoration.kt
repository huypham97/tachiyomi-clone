package com.example.tachiyomi_clone.common.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpaceItemGridDecoration(
    private val mSpace: Int
) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = mSpace
        if (parent.getChildAdapterPosition(view).mod(2) == 0)
            outRect.right = mSpace / 2
        else
            outRect.left = mSpace / 2
    }
}