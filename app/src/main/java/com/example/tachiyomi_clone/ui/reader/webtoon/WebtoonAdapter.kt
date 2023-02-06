package com.example.tachiyomi_clone.ui.reader.webtoon

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tachiyomi_clone.data.model.entity.PageEntity

class WebtoonAdapter(val viewer: WebtoonViewer) : RecyclerView.Adapter<WebtoonPageHolder>() {

    var items: MutableList<Any> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebtoonPageHolder {
        val view = ReaderPageImageView(viewer.fragment.requireContext())
        return WebtoonPageHolder(view, viewer)
    }

    override fun onBindViewHolder(holder: WebtoonPageHolder, position: Int) {
        val item = items[position]
        holder.bind(item as PageEntity)
    }

    override fun onViewRecycled(holder: WebtoonPageHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<Any>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}