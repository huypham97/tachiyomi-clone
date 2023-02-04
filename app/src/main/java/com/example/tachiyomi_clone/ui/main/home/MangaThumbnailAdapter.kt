package com.example.tachiyomi_clone.ui.main.home

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.databinding.RowRvMangaThumbnailBinding
import com.example.tachiyomi_clone.ui.base.BaseAdapter
import com.example.tachiyomi_clone.ui.base.BaseViewHolder

class MangaThumbnailAdapter : BaseAdapter<RowRvMangaThumbnailBinding, MangaEntity>() {

    companion object {
        private const val LIMIT_ITEM_COUNT = 10
    }

    var onSelectItemListener: ((MangaEntity) -> Unit)? = null

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.row_rv_manga_thumbnail

    override fun initViewHolder(
        holder: BaseViewHolder<RowRvMangaThumbnailBinding>,
        position: Int,
        item: MangaEntity?
    ) {
        Glide
            .with(holder.itemView)
            .asBitmap()
            .apply(
                RequestOptions().apply {
                    override(
                        holder.binding.ivMangaThumbnail.width,
                        holder.binding.ivMangaThumbnail.height
                    )
                }
            )
            .load(item?.thumbnailUrl)
            .into(holder.binding.ivMangaThumbnail)
    }

    override fun setEventListener(
        holder: BaseViewHolder<RowRvMangaThumbnailBinding>,
        position: Int,
        item: MangaEntity?
    ) {
        holder.binding.root.setOnClickListener {
            if (item != null) {
                onSelectItemListener?.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (super.getItemCount() > LIMIT_ITEM_COUNT) LIMIT_ITEM_COUNT else super.getItemCount()
    }
}