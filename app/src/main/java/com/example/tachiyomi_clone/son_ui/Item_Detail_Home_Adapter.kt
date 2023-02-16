package com.example.tachiyomi_clone.son_ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.SonItemDetailTrangChuBinding

class Item_Detail_Home_Adapter(val context: Context, var list: ArrayList<Item_Detail_Home>) : RecyclerView.Adapter<Item_Detail_Home_Adapter.Item_Detail_Home_ViewHolder>(){

    inner class Item_Detail_Home_ViewHolder(binding: SonItemDetailTrangChuBinding) : RecyclerView.ViewHolder(binding.root){
        val mBinding = binding as SonItemDetailTrangChuBinding

         fun onBind(context: Context, item: Item_Detail_Home){
            mBinding.ivItem.setImageResource(item.image)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item_Detail_Home_ViewHolder {
        return Item_Detail_Home_ViewHolder(SonItemDetailTrangChuBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Item_Detail_Home_ViewHolder, position: Int) {
        holder.onBind(context,list[position])
    }
}