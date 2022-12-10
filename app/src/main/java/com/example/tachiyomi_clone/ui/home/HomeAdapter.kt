package com.example.tachiyomi_clone.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.databinding.RowRecyclerViewMangaBinding


class HomeAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    private var list: MutableList<MangaEntity> = mutableListOf()
    private lateinit var context: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: RowRecyclerViewMangaBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.row_recycler_view_manga,
            parent, false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.tvMangaName.text = list[position].title
        Glide.with(context).load(list[position].thumbnail_url).into(holder.binding.ivMangaThumbnail)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun refreshList(data: List<MangaEntity>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}

class ItemViewHolder(val binding: RowRecyclerViewMangaBinding) :
    RecyclerView.ViewHolder(binding.root) {

}