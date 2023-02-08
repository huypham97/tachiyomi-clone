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
import com.example.tachiyomi_clone.ui.page.MangaPageActivity.Companion.MANGA_GENRE
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toKhongDau

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
    private var genreQuery: String? = null

    private val mangaPageAdapter = MangaPageAdapter()

    override fun getLayoutResId(): Int = R.layout.fragment_manga_page_detail

    override fun getLifeCycleOwner(): LifecycleOwner = this

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        initData()

        binding.toolbar.title = moduleSelected?.title ?: genreQuery
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

        moduleSelected?.let { module ->
            val items = module.type?.let { viewModel.fetchMangaPage(it) }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    module.type?.let {
                        items?.collectLatest {
                            mangaPageAdapter.submitData(it)
                        }
                    }
                }
            }
        }

        genreQuery?.let { genre ->
            val items = viewModel.fetchGenreManga(toKhongDau(genre))
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    items.collectLatest {
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

        genreQuery = arguments?.getString(MANGA_GENRE)
    }

    override fun setEventListeners() {
        super.setEventListeners()
        binding.toolbar.setNavigationOnClickListener {
            onHandleBackPressed()
        }

        mangaPageAdapter.onSelectItemListener = {
            val bundle = Bundle()
            bundle.putSerializable(MangaActivity.MANGA_ITEM, it)
            navController.navigate(R.id.action_manga_page_to_manga, bundle)
        }
    }
}