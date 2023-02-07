package com.example.tachiyomi_clone.ui.page.detail

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.widget.SpaceItemGridDecoration
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.databinding.FragmentMangaPageDetailBinding
import com.example.tachiyomi_clone.ui.base.BaseNavFragment
import com.example.tachiyomi_clone.ui.manga.MangaActivity
import com.example.tachiyomi_clone.ui.page.MangaPageActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MangaPageDetailFragment :
    BaseNavFragment<FragmentMangaPageDetailBinding, MangaPageDetailViewModel>(
        MangaPageDetailViewModel::class,
        R.id.nav_graph_page
    ) {

    companion object {
        @JvmStatic
        fun newInstance() = MangaPageDetailFragment()
    }

    private var moduleSelected: MangasPageEntity? = null

    private val mangaPageAdapter = MangaPageAdapter()

    override fun getLayoutResId(): Int = R.layout.fragment_manga_page_detail

    override fun getLifeCycleOwner(): LifecycleOwner = this

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        initData()

        binding.toolbar.title = moduleSelected?.title
        binding.toolbar.setNavigationOnClickListener {
            onHandleBackPressed()
        }

        this.binding.rvMangaPage.apply {
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(
                SpaceItemGridDecoration(
                    mSpace = context.resources.getDimension(R.dimen.et_dimen_20).toInt()
                )
            )
            adapter = mangaPageAdapter
        }

        val items = moduleSelected?.type?.let { viewModel.fetchMangaPage(it) }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                moduleSelected?.type?.let {
                    items?.collectLatest {
                        mangaPageAdapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun initData() {
        moduleSelected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(MangaPageActivity.MODULE_ITEM, MangasPageEntity::class.java)
        } else {
            arguments?.getSerializable(MangaPageActivity.MODULE_ITEM) as MangasPageEntity
        }
    }

    override fun setEventListeners() {
        super.setEventListeners()
        binding.toolbar.setNavigationOnClickListener {
            onHandleBackPressed()
        }

        mangaPageAdapter.onSelectLoanClientListener = {
            val bundle = Bundle()
            bundle.putSerializable(MangaActivity.MANGA_ITEM, it)
            navController.navigate(R.id.action_manga_page_to_manga, bundle)
        }
    }
}