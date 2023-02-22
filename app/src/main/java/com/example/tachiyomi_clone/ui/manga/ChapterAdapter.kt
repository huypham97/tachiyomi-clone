package com.example.tachiyomi_clone.ui.manga

import androidx.core.content.ContextCompat
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.databinding.RowRvChapterBinding
import com.example.tachiyomi_clone.ui.base.BaseAdapter
import com.example.tachiyomi_clone.ui.base.BaseViewHolder
import com.example.tachiyomi_clone.utils.getDateTime

class ChapterAdapter : BaseAdapter<RowRvChapterBinding, ChapterEntity>() {

    var onSelectChapterListener: ((ChapterEntity) -> Unit)? = null

    override fun getLayoutIdForViewType(viewType: Int): Int {
        return R.layout.row_rv_chapter
    }

    override fun initViewHolder(
        holder: BaseViewHolder<RowRvChapterBinding>,
        position: Int,
        item: ChapterEntity?
    ) {
        holder.binding.apply {
            tvChapterName.text = item?.name
            tvChapterDate.text = item?.dateUpload?.getDateTime("dd/MM/yyyy")
            tvChapterName.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (item?.read == true) R.color.color_999999 else R.color.color_F1F1F1
                )
            )
            tvChapterDate.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (item?.read == true) R.color.color_999999 else R.color.color_F1F1F1
                )
            )
        }
    }

    override fun setEventListener(
        holder: BaseViewHolder<RowRvChapterBinding>,
        position: Int,
        item: ChapterEntity?
    ) {
        super.setEventListener(holder, position, item)
        holder.binding.root.setOnClickListener {
            item?.let { onSelectChapterListener?.invoke(it) }
        }
    }
}
