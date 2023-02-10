package com.example.tachiyomi_clone.ui.main.favorite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.widget.SpaceItemDecoration
import com.example.tachiyomi_clone.databinding.FragmentFavoriteBinding
import com.example.tachiyomi_clone.ui.base.BaseGeneralFragment
import com.example.tachiyomi_clone.ui.common.ConfirmDialog
import com.example.tachiyomi_clone.utils.Constant

class FavoriteFragment :
    BaseGeneralFragment<FragmentFavoriteBinding, FavoriteViewModel>(FavoriteViewModel::class) {

    companion object {
        const val TAG = "FavoriteFragment"

        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }

    private val favoriteAdapter = FavoriteAdapter()
    private val mRefreshReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.fetchFavoriteMangasFromLocal()
        }

    }

    override fun getLayoutResId(): Int = R.layout.fragment_favorite

    override fun getLifeCycleOwner(): LifecycleOwner = this

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(
                mRefreshReceiver,
                IntentFilter(Constant.RECEIVER_ID)
            )
    }
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

        viewModel.fetchFavoriteMangasFromLocal()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(mRefreshReceiver);
    }

    override fun setEventListeners() {
        super.setEventListeners()
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
            binding.cbDelete.isChecked = false
            favoriteAdapter.setAllCheckBoxSelect(false)
        }

        binding.cbDelete.setOnCheckedChangeListener { _, isChecked ->
            binding.cbDelete.setOnClickListener {
                favoriteAdapter.setAllCheckBoxSelect(isChecked)
            }
        }

        favoriteAdapter.onCheckedDeleteBoxListener = { isSelectAll, isEnableDelete ->
            binding.cbDelete.isChecked = isSelectAll
            binding.ivButtonDelete.setImageResource(if (isEnableDelete) R.drawable.ic_bin_selected else R.drawable.ic_bin_unselected)
            binding.ivButtonDelete.isEnabled = isEnableDelete
        }

        binding.ivButtonDelete.setOnClickListener {
            ConfirmDialog.show(parentFragmentManager) {
                favoriteAdapter.clickAllCheckBoxVisibility()
                binding.rlToolbarDelete.isVisible = false
                binding.toolbar.isVisible = true
                binding.cbDelete.isChecked = false
                favoriteAdapter.setAllCheckBoxSelect(false)
            }
        }

        viewModel.listFavorite.observe(this) {
            favoriteAdapter.refreshList(it)
        }
    }

}