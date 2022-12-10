package com.example.tachiyomi_clone.ui.home

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.ActivityHomeBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity

class HomeActivity :
    BaseActivity<ActivityHomeBinding, HomeViewModel>() {

    private val homeAdapter = HomeAdapter()

    override val modelClass: Class<HomeViewModel> = HomeViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_home

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        this.binding.rvManga.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = homeAdapter
        }
    }

    override fun setEventListener() {
        super.setEventListener()
        viewModel.getPopularManga(page = 1) {
            it.mangas?.let { list -> homeAdapter.refreshList(list) }
        }
    }
}