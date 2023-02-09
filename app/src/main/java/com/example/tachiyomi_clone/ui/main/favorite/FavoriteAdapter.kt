package com.example.tachiyomi_clone.ui.main.favorite

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.databinding.RowRvMangaFavoriteBinding
import com.example.tachiyomi_clone.ui.base.BaseAdapter
import com.example.tachiyomi_clone.ui.base.BaseViewHolder

class FavoriteAdapter : BaseAdapter<RowRvMangaFavoriteBinding, MangaEntity>() {

    private var isShowAllCheckBox = false

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.row_rv_manga_favorite

    override fun initViewHolder(
        holder: BaseViewHolder<RowRvMangaFavoriteBinding>,
        position: Int,
        item: MangaEntity?
    ) {
        super.initViewHolder(holder, position, item)
        Glide.with(holder.itemView).asBitmap().apply(RequestOptions().apply {
            override(
                holder.binding.ivMangaThumbnail.width,
                holder.binding.ivMangaThumbnail.height
            )
        })
            .load(getItem(position)?.thumbnailUrl)
            .into(holder.binding.ivMangaThumbnail)
        holder.binding.tvMangaTitle.text = item?.title
        holder.binding.tvMangaAuthor.text = item?.author
        holder.binding.cbDelete.isVisible = isShowAllCheckBox
    }

    fun clickAllCheckBoxVisibility() {
        this.isShowAllCheckBox = !(this.isShowAllCheckBox)
        notifyDataSetChanged()
    }
}