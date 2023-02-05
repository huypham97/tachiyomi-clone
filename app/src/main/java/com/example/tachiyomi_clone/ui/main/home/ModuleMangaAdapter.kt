package com.example.tachiyomi_clone.ui.main.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.databinding.RowRvContainerModuleBinding
import com.example.tachiyomi_clone.ui.base.BaseAdapter
import com.example.tachiyomi_clone.ui.base.BaseViewHolder
import com.example.tachiyomi_clone.common.widget.SpaceItemDecoration

class ModuleMangaAdapter : BaseAdapter<RowRvContainerModuleBinding, MangasPageEntity>() {

    var onSelectItemListener: ((MangaEntity) -> Unit)? = null

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.row_rv_container_module

    override fun initViewHolder(
        holder: BaseViewHolder<RowRvContainerModuleBinding>,
        position: Int,
        item: MangasPageEntity?
    ) {
        val mangaThumbnailAdapter = MangaThumbnailAdapter()
        holder.binding.tvModuleTitle.text = item?.title
        holder.binding.rvManga.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                SpaceItemDecoration(
                    mSpace = context.resources.getDimension(R.dimen.et_dimen_12).toInt(),
                    mOrientation = LinearLayoutManager.HORIZONTAL
                )
            )
            adapter = mangaThumbnailAdapter
        }
        item?.mangas?.let { mangaThumbnailAdapter.refreshList(it) }
        mangaThumbnailAdapter.onSelectItemListener = {
            this.onSelectItemListener?.invoke(it)
        }
    }

    override fun setEventListener(
        holder: BaseViewHolder<RowRvContainerModuleBinding>,
        position: Int,
        item: MangasPageEntity?
    ) {
        holder.binding.tvAll.setOnClickListener {

        }
    }
}