package com.example.tachiyomi_clone.ui.main.home

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.widget.SpaceItemDecoration
import com.example.tachiyomi_clone.databinding.FragmentHomeBinding
import com.example.tachiyomi_clone.ui.base.BaseGeneralFragment
import com.example.tachiyomi_clone.ui.manga.MangaActivity
import com.example.tachiyomi_clone.ui.manga.MangaActivity.Companion.MANGA_ITEM
import com.example.tachiyomi_clone.ui.page.MangaPageActivity

class HomeFragment : BaseGeneralFragment<FragmentHomeBinding, HomeViewModel>(HomeViewModel::class) {

    companion object {
        const val TAG = "HomeFragment"

        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var moduleMangaAdapter: ModuleMangaAdapter

    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun getLifeCycleOwner(): LifecycleOwner = this

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        bannerAdapter = BannerAdapter()
        moduleMangaAdapter = ModuleMangaAdapter()
        binding.vpBanner.adapter = bannerAdapter
        binding.dotIndicator.attachViewPager2(binding.vpBanner)
        binding.rvMangaModule.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moduleMangaAdapter
            addItemDecoration(
                SpaceItemDecoration(
                    context.resources.getDimension(R.dimen.et_dimen_12).toInt(),
                )
            )
        }
        viewModel.fetchSuggestManga()
        viewModel.fetchListModuleManga()
    }

    override fun setEventListeners() {
        super.setEventListeners()
        viewModel.popularManga.observe(this) {
            bannerAdapter.refreshList(it)
        }
        viewModel.modulesManga.observe(this) {
            moduleMangaAdapter.refreshList(it)
        }
        viewModel.isLoading.observe(this) {
            binding.pbLoading.isVisible = it
        }
        moduleMangaAdapter.onSelectItemListener = {
            val intent = Intent(context, MangaActivity::class.java)
            intent.putExtra(MANGA_ITEM, it)
            startActivity(intent)
        }
        moduleMangaAdapter.onSelectSeeAllListener = {
            val intent = Intent(context, MangaPageActivity::class.java)
            intent.putExtra(MangaPageActivity.MODULE_ITEM, it)
            startActivity(intent)
        }
    }
}