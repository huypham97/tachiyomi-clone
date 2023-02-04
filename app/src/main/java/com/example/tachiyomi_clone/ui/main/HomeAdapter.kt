package com.example.tachiyomi_clone.ui.main

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
import com.example.tachiyomi_clone.databinding.RowRvMangaThumbnailBinding

class HomeAdapter : PagingDataAdapter<MangaEntity, HomeViewHolder>(HomeComparator) {

    private lateinit var context: Context

    var onSelectLoanClientListener: ((MangaEntity) -> Unit)? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: RowRvMangaThumbnailBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.row_rv_manga_thumbnail, parent, false
        )
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        initViewHolder(holder, position, getItem(position))
        setEventListener(holder, position, getItem(position))
    }

    private fun initViewHolder(holder: HomeViewHolder, position: Int, item: MangaEntity?) {
        Glide.with(holder.itemView).asBitmap().apply(RequestOptions().apply {
            override(
                holder.binding.ivMangaThumbnail.width,
                holder.binding.ivMangaThumbnail.height
            )
        })
            .load(getItem(position)?.thumbnailUrl)
            .into(holder.binding.ivMangaThumbnail)
    }

    private fun setEventListener(holder: HomeViewHolder, position: Int, item: MangaEntity?) {
        holder.binding.root.setOnClickListener {
            if (item != null) {
                onSelectLoanClientListener?.invoke(item)
            }
        }
    }

    object HomeComparator : DiffUtil.ItemCallback<MangaEntity>() {
        override fun areItemsTheSame(oldItem: MangaEntity, newItem: MangaEntity): Boolean {
            return oldItem.url == newItem.url
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: MangaEntity, newItem: MangaEntity): Boolean {
            return oldItem == newItem
        }

    }
}

class HomeViewHolder(val binding: RowRvMangaThumbnailBinding) :
    RecyclerView.ViewHolder(binding.root) {

}