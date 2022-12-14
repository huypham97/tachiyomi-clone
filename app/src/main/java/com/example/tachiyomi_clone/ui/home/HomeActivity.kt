package com.example.tachiyomi_clone.ui.home

import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.ActivityHomeBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity
import com.example.tachiyomi_clone.usecase.home.HomeUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity :
    BaseActivity<ActivityHomeBinding, HomeViewModel>() {

    private val homeAdapter = HomeAdapter()

    override val modelClass: Class<HomeViewModel> = HomeViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_home

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        this.binding.llPopular.setOnClickListener {
            this.binding.llPopular.setBackgroundResource(R.drawable.shape_bg_blue_corner_16)
            this.binding.llLatest.setBackgroundResource(R.drawable.shape_bg_trans_stroke_black_corner_16)
        }
        this.binding.llLatest.setOnClickListener {
            this.binding.llLatest.setBackgroundResource(R.drawable.shape_bg_blue_corner_16)
            this.binding.llPopular.setBackgroundResource(R.drawable.shape_bg_trans_stroke_black_corner_16)
        }
        this.binding.rvManga.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = homeAdapter
            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.divider, theme)
            addItemDecoration(
                DividerItemDecoration(
                    this@HomeActivity,
                    DividerItemDecoration.HORIZONTAL
                ).apply { setDrawable(drawable!!) })
            addItemDecoration(
                DividerItemDecoration(
                    this@HomeActivity,
                    DividerItemDecoration.VERTICAL
                ).apply { setDrawable(drawable!!) })
        }

        val items = viewModel.listManga(HomeUseCase.QUERY_POPULAR)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                items.collectLatest {
                    homeAdapter.submitData(it)
                }
            }
        }
    }

    override fun setEventListener() {
        super.setEventListener()
    }
}