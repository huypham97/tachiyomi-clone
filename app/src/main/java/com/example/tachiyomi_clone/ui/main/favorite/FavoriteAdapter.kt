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
    private var isAllCheckBoxSelected = false
    var onCheckedDeleteBoxListener: ((Boolean, Boolean) -> Unit)? = null
    var onItemSelectListener: ((MangaEntity) -> Unit)? = null

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
        if (item != null) {
            holder.binding.cbDelete.isChecked = item.isChecked
        }
        if (holder.binding.cbDelete.isChecked != isAllCheckBoxSelected)  {
            holder.binding.cbDelete.isChecked = isAllCheckBoxSelected
        }
    }

    override fun setEventListener(
        holder: BaseViewHolder<RowRvMangaFavoriteBinding>,
        position: Int,
        item: MangaEntity?
    ) {
        super.setEventListener(holder, position, item)
        holder.binding.cbDelete.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) this.isAllCheckBoxSelected = false
            item?.isChecked = isChecked
            var count = 0
            val size = getListItem().size
            for (i in 0 until size) {
                if (getListItem()[i].isChecked) {
                    count++
                }
            }
            onCheckedDeleteBoxListener?.invoke(count == size, count != 0)
        }
        holder.binding.root.setOnClickListener {
            if (item != null) {
                onItemSelectListener?.invoke(item)
            }
        }
    }

    fun clickAllCheckBoxVisibility() {
        this.isShowAllCheckBox = !(this.isShowAllCheckBox)
        notifyDataSetChanged()
    }

    fun setAllCheckBoxSelect(isCheck: Boolean) {
        this.isAllCheckBoxSelected = isCheck
        notifyDataSetChanged()
    }
}