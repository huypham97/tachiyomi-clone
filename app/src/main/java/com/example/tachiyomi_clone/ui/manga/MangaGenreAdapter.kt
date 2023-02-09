package com.example.tachiyomi_clone.ui.manga

import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.GenreEntity
import com.example.tachiyomi_clone.databinding.RowRvMangaGenreBinding
import com.example.tachiyomi_clone.ui.base.BaseAdapter
import com.example.tachiyomi_clone.ui.base.BaseViewHolder

class MangaGenreAdapter : BaseAdapter<RowRvMangaGenreBinding, GenreEntity>() {

    var onSelectItemListener: ((GenreEntity) -> Unit)? = null

    override fun getLayoutIdForViewType(viewType: Int): Int {
        return R.layout.row_rv_manga_genre
    }

    override fun initViewHolder(
        holder: BaseViewHolder<RowRvMangaGenreBinding>,
        position: Int,
        item: GenreEntity?
    ) {
        holder.binding.tvGenre.text = item?.title
    }

    override fun setEventListener(
        holder: BaseViewHolder<RowRvMangaGenreBinding>,
        position: Int,
        item: GenreEntity?
    ) {
        super.setEventListener(holder, position, item)
        holder.binding.root.setOnClickListener {
            if (item != null) {
                onSelectItemListener?.invoke(item)
            }
        }
    }

}