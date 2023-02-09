package com.example.tachiyomi_clone.ui.main.favorite

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.widget.SpaceItemDecoration
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.databinding.FragmentFavoriteBinding
import com.example.tachiyomi_clone.ui.base.BaseGeneralFragment

class FavoriteFragment :
    BaseGeneralFragment<FragmentFavoriteBinding, FavoriteViewModel>(FavoriteViewModel::class) {

    companion object {
        const val TAG = "FavoriteFragment"

        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }

    private val favoriteAdapter = FavoriteAdapter()
    private val list = listOf(
        MangaEntity.create().copy(
            thumbnailUrl = "https://gamek.mediacdn.vn/133514250583805952/2021/6/17/ma1-16239080157491600788916.png",
            title = "Blue Lock",
            author = "Yamazaki"
        ),
        MangaEntity.create().copy(
            thumbnailUrl = "https://gamek.mediacdn.vn/133514250583805952/2021/6/17/ma1-16239080157491600788916.png",
            title = "Blue Lock",
            author = "Yamazaki"
        ),
        MangaEntity.create().copy(
            thumbnailUrl = "https://gamek.mediacdn.vn/133514250583805952/2021/6/17/ma1-16239080157491600788916.png",
            title = "Blue Lock",
            author = "Yamazaki"
        ),
        MangaEntity.create().copy(
            thumbnailUrl = "https://gamek.mediacdn.vn/133514250583805952/2021/6/17/ma1-16239080157491600788916.png",
            title = "Blue Lock",
            author = "Yamazaki"
        ),
    )

    override fun getLayoutResId(): Int = R.layout.fragment_favorite

    override fun getLifeCycleOwner(): LifecycleOwner = this

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        binding.rvMangaPage.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                SpaceItemDecoration(
                    mSpace = context.resources.getDimension(R.dimen.et_dimen_12).toInt(),
                )
            )
            adapter = favoriteAdapter
        }
        favoriteAdapter.refreshList(list)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.app_bar_edit -> {
                    favoriteAdapter.clickAllCheckBoxVisibility()
                    binding.rlToolbarDelete.isVisible = true
                    binding.toolbar.isVisible = false
                }
            }
            return@setOnMenuItemClickListener true
        }
        binding.ivButtonClose.setOnClickListener {
            favoriteAdapter.clickAllCheckBoxVisibility()
            binding.rlToolbarDelete.isVisible = false
            binding.toolbar.isVisible = true
        }
    }

}