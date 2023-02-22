package com.example.tachiyomi_clone.ui.manga.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.widget.VerticalSpacingDecoration
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.databinding.FragmentMangaDetailBinding
import com.example.tachiyomi_clone.ui.base.BaseNavFragment
import com.example.tachiyomi_clone.ui.manga.ChapterAdapter
import com.example.tachiyomi_clone.ui.manga.MangaActivity.Companion.MANGA_ITEM
import com.example.tachiyomi_clone.ui.manga.MangaGenreAdapter
import com.example.tachiyomi_clone.ui.page.MangaPageActivity
import com.example.tachiyomi_clone.ui.reader.ReaderFragment
import com.example.tachiyomi_clone.utils.Constant
import com.example.tachiyomi_clone.utils.loadByHtml
import com.example.tachiyomi_clone.utils.setColor
import com.example.tachiyomi_clone.utils.system.ScreenUtils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import jp.wasabeef.glide.transformations.BlurTransformation


class MangaDetailFragment : BaseNavFragment<FragmentMangaDetailBinding, MangaDetailViewModel>(
    MangaDetailViewModel::class, R.id.nav_graph_manga
) {

    companion object {
        @JvmStatic
        fun newInstance() = MangaDetailFragment()

        private const val LOAD_MORE_PEAK = 50
    }

    private var mangaSelected: MangaEntity? = null
    private val genreAdapter = MangaGenreAdapter()
    private val chapterAdapter = ChapterAdapter()
    private var count = 0
    private var isLoadingMore = false
    private var isShow = true
    private var scrollRange = -1

    private val windowInsetsController by lazy {
        WindowInsetsControllerCompat(
            requireActivity().window, binding.root
        )
    }

    override fun getLayoutResId(): Int = R.layout.fragment_manga_detail

    override fun getLifeCycleOwner(): LifecycleOwner = this

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        initData()
        mangaSelected?.let {
            binding.tbHeader.title = it.title
            binding.tvMangaTitle.text = it.title
            viewModel.fetchMangaFromSource(it)
        }

        binding.rvMangaGenre.apply {
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            adapter = genreAdapter
        }

        binding.rvChapter.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                VerticalSpacingDecoration(
                    requireContext(), R.drawable.divider
                )
            )
            adapter = chapterAdapter
        }

        binding.appBarHeader.addOnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.tbHeader.title = mangaSelected?.title
                isShow = true
            } else if (isShow) {
                binding.tbHeader.title =
                    " " //careful there should a space between double quote otherwise it wont work
                isShow = false
            }
        }

        binding.tvChapterSortTitle.text =
            resources.getText(if (viewModel.ascendingSort) R.string.newest else R.string.oldest)

        ScreenUtils.getStatusBarHeight(requireContext()).let {
            binding.tbHeader.apply {
                val params = layoutParams as FrameLayout.LayoutParams
                params.setMargins(0, it, 0, 0)
            }
            binding.clContent.apply {
                val params = layoutParams as FrameLayout.LayoutParams
                params.setMargins(0, it + ScreenUtils.getAppBarHeight(requireContext()), 0, 0)
            }
        }

    }

    private fun initData() {
        mangaSelected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(MANGA_ITEM, MangaEntity::class.java)
        } else {
            arguments?.getSerializable(MANGA_ITEM) as MangaEntity
        }
    }

    override fun setEventListeners() {
        super.setEventListeners()
        binding.tbHeader.setNavigationOnClickListener {
            viewModel.onBackPressed()
        }

        binding.tbHeader.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.app_bar_heart -> {
                    viewModel.setFavoriteManga()
                    binding.tbHeader.menu.findItem(R.id.app_bar_heart)
                        .setIcon(if (viewModel.isFavorite) R.drawable.ic_heart_selected else R.drawable.ic_heart)
                    val intent = Intent(Constant.RECEIVER_ID)
                    LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(intent)
                }
            }
            return@setOnMenuItemClickListener true
        }

        binding.tvReadFirst.setOnClickListener {
            viewModel.chapters.value?.let {
                navigateReaderActivity(it[it.size - 1])
                viewModel.markChapterRead(it[it.size - 1])
            }
        }

        binding.tvReadLast.setOnClickListener {
            viewModel.chapters.value?.let {
                navigateReaderActivity(it[0])
                viewModel.markChapterRead(it[0])
            }
        }

        viewModel.manga.observe(this) { manga ->
            binding.tbHeader.menu.findItem(R.id.app_bar_heart)
                .setIcon(if (viewModel.isFavorite) R.drawable.ic_heart_selected else R.drawable.ic_heart)
            Glide.with(this).asBitmap().apply(RequestOptions().apply {
                override(
                    binding.ivMangaThumbnail.width, binding.ivMangaThumbnail.height
                )
            }).load(manga.thumbnailUrl).into(binding.ivMangaThumbnail)
            Glide.with(this).asBitmap().apply(RequestOptions().apply {
                override(
                    binding.ivBackground.width, binding.ivBackground.height
                )
            }).load(manga.thumbnailUrl)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                .into(binding.ivBackground)
            binding.tvMangaStatus.text = "${resources.getString(R.string.status)}: ${
                resources.getString(manga.getStatus())
                    .setColor(requireContext(), R.color.color_FFB340)
            }".loadByHtml()
            binding.tvMangaAuthor.text = "${resources.getString(R.string.author)}: ${
                manga.author?.setColor(requireContext(), R.color.color_FFB340)
            }".loadByHtml()
            binding.etvMangaDescribe.setNoteContent(manga.description)
            genreAdapter.refreshList(manga.genre)
        }

        viewModel.chapters.observe(this) { chapters ->
            binding.tvChaptersCount.text = "Đã ra ${chapters.size} chương"
            setChapterList(chapters)
        }

        viewModel.isLoading.observe(this) {
            binding.pbLoading.isVisible = it
        }

        chapterAdapter.onSelectChapterListener = {
            viewModel.markChapterRead(it)
            navigateReaderActivity(it)
        }

        binding.ivChapterSort.setOnClickListener {
            count = 0
            viewModel.sortChapterList()
            binding.tvChapterSortTitle.text =
                resources.getText(if (viewModel.ascendingSort) R.string.newest else R.string.oldest)
        }

        genreAdapter.onSelectItemListener = {
            val intent = Intent(context, MangaPageActivity::class.java)
            intent.putExtra(MangaPageActivity.MANGA_GENRE, it)
            startActivity(intent)
        }

        initScrollListener()
    }

    private fun initScrollListener() {
        binding.nsv.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                if (!isLoadingMore) {
                    loadMore()
                    isLoadingMore = true
                }
            }
        })
    }

    private fun loadMore() {
        Handler(Looper.getMainLooper()).postDelayed({
            isLoadingMore = false
            viewModel.chapters.value?.let {
                setChapterList(it)
            }
        }, 100)
    }

    private fun setChapterList(list: List<ChapterEntity>) {
        if (count == list.size) return
        count += if (list.size - count > LOAD_MORE_PEAK) LOAD_MORE_PEAK else list.size - count
        chapterAdapter.refreshList(list.take(count))
    }

    private fun navigateReaderActivity(chapter: ChapterEntity) {
        viewModel.selectedChapter = chapter
        val bundle = Bundle()
        bundle.putSerializable(ReaderFragment.MANGA_VALUE, mangaSelected)
        navController.navigate(R.id.action_manga_to_reader, bundle)
    }

    override fun onResume() {
        super.onResume()
        initStatusBars()
    }

    private fun initStatusBars() {
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}