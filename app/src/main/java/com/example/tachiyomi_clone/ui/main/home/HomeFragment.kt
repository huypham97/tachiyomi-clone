package com.example.tachiyomi_clone.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.FragmentHomeBinding
import com.example.tachiyomi_clone.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    companion object {
        const val TAG = "HomeFragment"

        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    private lateinit var bannerAdapter: BannerAdapter

    override val modelClass: Class<HomeViewModel>
        get() = HomeViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun getLifeCycleOwner(): LifecycleOwner = this

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        bannerAdapter = BannerAdapter()
        binding.vpBanner.adapter = bannerAdapter
        binding.dotIndicator.attachViewPager2(binding.vpBanner)
        viewModel.fetchPopularManga()
    }

    override fun setEventListeners() {
        super.setEventListeners()
        viewModel.popularManga.observe(this) {
            bannerAdapter.refreshList(it)
        }
        viewModel.isLoading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }
}