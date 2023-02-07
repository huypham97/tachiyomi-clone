package com.example.tachiyomi_clone.ui.page.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.databinding.RowRvMangaPageBinding

class MangaPageAdapter : PagingDataAdapter<MangaEntity, MangaPageViewHolder>(MangaPageComparator) {

    private lateinit var context: Context

    var onSelectLoanClientListener: ((MangaEntity) -> Unit)? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaPageViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: RowRvMangaPageBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.row_rv_manga_page, parent, false
        )
        return MangaPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MangaPageViewHolder, position: Int) {
        initViewHolder(holder, position, getItem(position))
        setEventListener(holder, position, getItem(position))
    }

    private fun initViewHolder(holder: MangaPageViewHolder, position: Int, item: MangaEntity?) {
        Glide.with(holder.itemView).asBitmap().apply(RequestOptions().apply {
            override(
                holder.binding.ivMangaThumbnail.width,
                holder.binding.ivMangaThumbnail.height
            )
        })
            .load(item?.thumbnailUrl)
            .into(holder.binding.ivMangaThumbnail)
        holder.binding.tvMangaTitle.text = item?.title
    }

    private fun setEventListener(holder: MangaPageViewHolder, position: Int, item: MangaEntity?) {
        holder.binding.root.setOnClickListener {
            if (item != null) {
                onSelectLoanClientListener?.invoke(item)
            }
        }
    }

    object MangaPageComparator : DiffUtil.ItemCallback<MangaEntity>() {
        override fun areItemsTheSame(oldItem: MangaEntity, newItem: MangaEntity): Boolean {
            return oldItem.url == newItem.url
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: MangaEntity, newItem: MangaEntity): Boolean {
            return oldItem == newItem
        }

    }
}

class MangaPageViewHolder(val binding: RowRvMangaPageBinding) :
    RecyclerView.ViewHolder(binding.root) {

}