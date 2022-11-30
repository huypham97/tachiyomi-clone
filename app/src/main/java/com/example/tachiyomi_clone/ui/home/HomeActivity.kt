package com.example.tachiyomi_clone.ui.home

import android.os.Bundle
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.ActivityHomeBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity

class HomeActivity :
    BaseActivity<ActivityHomeBinding, HomeViewModel>() {

    override val modelClass: Class<HomeViewModel> = HomeViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_home

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        viewModel.getPopularManga()
    }
}