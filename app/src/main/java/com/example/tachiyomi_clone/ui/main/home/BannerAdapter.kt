package com.example.tachiyomi_clone.ui.main.home

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.databinding.PageMangaBannerBinding
import com.example.tachiyomi_clone.ui.base.BaseAdapter
import com.example.tachiyomi_clone.ui.base.BaseViewHolder

class BannerAdapter : BaseAdapter<PageMangaBannerBinding, MangaEntity>() {
    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.page_manga_banner

    override fun initViewHolder(
        holder: BaseViewHolder<PageMangaBannerBinding>,
        position: Int,
        item: MangaEntity?
    ) {
        Glide.with(holder.itemView).asBitmap().apply(RequestOptions().apply {
            override(
                holder.binding.ivMangaThumbnail.width,
                holder.binding.ivMangaThumbnail.height
            )
        })
            .load(getItem(position)?.thumbnailUrl)
            .into(holder.binding.ivMangaThumbnail)
    }
}