package com.example.tachiyomi_clone.son_ui


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.RowRecyclerViewMangaBinding
import com.example.tachiyomi_clone.databinding.SonVp2BannerBinding


class ViewPagerAdapter(val context: Context) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(binding: SonVp2BannerBinding) : RecyclerView.ViewHolder(binding.root){
        var mBinding = binding as SonVp2BannerBinding
        fun onBind(context: Context, item: Int){
            mBinding.ivBanner.setImageResource(item)
        }
    }

    private val images = intArrayOf(
        R.drawable.banner,
        R.drawable.banner,
        R.drawable.banner,
        R.drawable.banner,
        R.drawable.banner,
        R.drawable.banner,

        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(SonVp2BannerBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.onBind(context, images[position])
    }
}