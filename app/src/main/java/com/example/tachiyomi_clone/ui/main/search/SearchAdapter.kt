package com.example.tachiyomi_clone.ui.main.search

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
import com.example.tachiyomi_clone.databinding.RowRvMangaSearchBinding

class SearchAdapter : PagingDataAdapter<MangaEntity, SearchViewHolder>(SearchComparator) {

    private lateinit var context: Context

    var onSelectItemListener: ((MangaEntity) -> Unit)? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: RowRvMangaSearchBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.row_rv_manga_search, parent, false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        initViewHolder(holder, position, getItem(position))
        setEventListener(holder, position, getItem(position))
    }

    private fun initViewHolder(holder: SearchViewHolder, position: Int, item: MangaEntity?) {
        Glide.with(holder.itemView).asBitmap().apply(RequestOptions().apply {
            override(
                holder.binding.ivMangaThumbnail.width,
                holder.binding.ivMangaThumbnail.height
            )
        })
            .load(item?.thumbnailUrl)
            .into(holder.binding.ivMangaThumbnail)
        holder.binding.tvMangaTitle.text = item?.title
        holder.binding.tvMangaAuthor.text = item?.author
        holder.binding.tvMangaGenre.text = item?.genre?.joinToString(",")
    }

    private fun setEventListener(holder: SearchViewHolder, position: Int, item: MangaEntity?) {
        holder.binding.root.setOnClickListener {
            if (item != null) {
                onSelectItemListener?.invoke(item)
            }
        }
    }

    object SearchComparator : DiffUtil.ItemCallback<MangaEntity>() {
        override fun areItemsTheSame(oldItem: MangaEntity, newItem: MangaEntity): Boolean {
            return oldItem.url == newItem.url
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: MangaEntity, newItem: MangaEntity): Boolean {
            return oldItem == newItem
        }

    }
}

class SearchViewHolder(val binding: RowRvMangaSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {

}