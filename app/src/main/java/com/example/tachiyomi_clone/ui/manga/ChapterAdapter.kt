package com.example.tachiyomi_clone.ui.manga

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
