package com.example.tachiyomi_clone.son_ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.SonItemTrangChuBinding

class Item_Home_Adapter(var context: Context, var list: ArrayList<Item_Home>) : RecyclerView.Adapter<Item_Home_Adapter.Item_Home_ViewHolder>() {

    inner class Item_Home_ViewHolder(binding: SonItemTrangChuBinding) : RecyclerView.ViewHolder(binding.root){
        val mBinding = binding
        fun onBind(context: Context, item: Item_Home){
            mBinding.tvTatCa.text = item.title

            val listItem = ArrayList<Item_Detail_Home>()

            val itemDetailHome1 = Item_Detail_Home(R.drawable.avt_detail_ryc)
            val itemDetailHome2 = Item_Detail_Home(R.drawable.avt_detail_ryc)
            val itemDetailHome3 = Item_Detail_Home(R.drawable.avt_detail_ryc)
            val itemDetailHome4 = Item_Detail_Home(R.drawable.avt_detail_ryc)
            val itemDetailHome5 = Item_Detail_Home(R.drawable.avt_detail_ryc)

            listItem.add(itemDetailHome1)
            listItem.add(itemDetailHome2)
            listItem.add(itemDetailHome3)
            listItem.add(itemDetailHome4)
            listItem.add(itemDetailHome5)

            val adapter = Item_Detail_Home_Adapter(context, listItem)



            mBinding.rycItemDetail.adapter = adapter
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item_Home_ViewHolder {
        return Item_Home_ViewHolder(SonItemTrangChuBinding.inflate(LayoutInflater.from(context), parent, false))
    }



    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Item_Home_ViewHolder, position: Int) {
        holder.onBind(context, list[position])
    }
}