package com.example.tachiyomi_clone.son_ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tachiyomi_clone.databinding.SonItemDetailTrangChuBinding
import com.example.tachiyomi_clone.databinding.SonVp2BannerBinding

class Hozi_Adapter(var context:Context, var list: ArrayList<Item_Detail_Home>) : RecyclerView.Adapter<Hozi_Adapter.Hozi_ViewHolder>() {
    inner class Hozi_ViewHolder(binding: SonItemDetailTrangChuBinding) : RecyclerView.ViewHolder(binding.root){
        val mBinding = binding

        fun onBind(context: Context, item: Item_Detail_Home){
            mBinding.ivItem.setImageResource(item.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Hozi_ViewHolder {
        return Hozi_ViewHolder(SonItemDetailTrangChuBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Hozi_ViewHolder, position: Int) {
        holder.onBind(context, list[position])
    }
}