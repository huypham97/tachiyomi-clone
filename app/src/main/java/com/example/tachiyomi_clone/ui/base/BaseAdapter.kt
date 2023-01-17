package com.example.tachiyomi_clone.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<B : ViewDataBinding, E> : RecyclerView.Adapter<BaseViewHolder<B>>() {

    protected lateinit var context: Context

    private val listObjects: MutableList<E> = mutableListOf()

    private val items: MutableList<E>
        get() = listObjects

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<B> {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: B = DataBindingUtil.inflate(
            layoutInflater,
            getLayoutIdForViewType(viewType),
            parent, false
        )
        return BaseViewHolder(binding)
    }

    protected abstract fun getLayoutIdForViewType(viewType: Int): Int

    override fun onBindViewHolder(holder: BaseViewHolder<B>, position: Int) {
        initViewHolder(holder, position, getItem(position))
        setEventListener(holder, position, getItem(position))
    }

    override fun getItemCount(): Int = listObjects.size

    fun getItem(position: Int): E? {
        return if (position in 0 until itemCount) {
            items[position]
        } else
            null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshList(data: Collection<E>) {
        listObjects.clear()
        listObjects.addAll(data)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearList() {
        listObjects.clear()
        notifyDataSetChanged()
    }

    fun addItems(data: Collection<E>) {
        val count = listObjects.size
        listObjects.addAll(data)
        notifyItemRangeInserted(count, data.size)
    }

    fun addItem(data: E) {
        val size = listObjects.size
        listObjects.add(data)
        notifyItemInserted(size)
    }

    fun addItemAtPosition(data: E, position: Int) {
        if (position in 0 until itemCount) {
            listObjects.add(position, data)
            notifyItemInserted(position)
        }
    }

    fun removeItemAtPosition(position: Int) {
        if (position in 0 until itemCount) {
            listObjects.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    open fun initViewHolder(holder: BaseViewHolder<B>, position: Int, item: E?) {}

    open fun setEventListener(holder: BaseViewHolder<B>, position: Int, item: E?) {}
}
