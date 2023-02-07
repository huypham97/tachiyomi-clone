package com.example.tachiyomi_clone.ui.reader

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.navGraphViewModels
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.listener.SimpleAnimationListener
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.databinding.FragmentReaderBinding
import com.example.tachiyomi_clone.ui.base.BaseNavFragment
import com.example.tachiyomi_clone.ui.common.ListIndexBottomSheetDialog
import com.example.tachiyomi_clone.ui.manga.detail.MangaDetailViewModel
import com.example.tachiyomi_clone.ui.reader.webtoon.WebtoonViewer
import com.example.tachiyomi_clone.utils.system.applySystemAnimatorScale

class ReaderFragment :
    BaseNavFragment<FragmentReaderBinding, ReaderViewModel>(
        ReaderViewModel::class,
        R.id.nav_graph_manga
    ) {
    companion object {
        @JvmStatic
        fun newInstance() = ReaderFragment()

        const val MANGA_VALUE = "MANGA_VALUE"
    }

    private lateinit var viewer: WebtoonViewer
    var menuVisible = false
        private set
    private val windowInsetsController by lazy {
        WindowInsetsControllerCompat(
            requireActivity().window,
            binding.root
        )
    }
    private val mangaDetailViewModel by navGraphViewModels<MangaDetailViewModel>(
        navGraphId = R.id.nav_graph_manga,
        factoryProducer = { viewModelFactory })

    private var mangaSelected: MangaEntity? = null

    override fun getLayoutResId(): Int = R.layout.fragment_reader

    override fun getLifeCycleOwner(): LifecycleOwner = this

    override fun initViews(savedInstanceState: Bundle?) {
        initData()

        viewer = WebtoonViewer(this)
        binding.tbHeader.title = mangaSelected?.title
        binding.tvChapterName.text = mangaDetailViewModel.selectedChapter?.name
        viewModel.getPages(mangaDetailViewModel.selectedChapter?.url ?: "")
        binding.flPage.addView(viewer.getView())
        binding.tbHeader.menu.getItem(0).isVisible = false

        setNavButtonStatus()

        initMenu()
    }

    private fun initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mangaSelected = arguments?.getSerializable(MANGA_VALUE, MangaEntity::class.java)
        } else {
            mangaSelected = arguments?.getSerializable(MANGA_VALUE) as MangaEntity
        }
    }

    private fun setNavButtonStatus() {
        (mangaDetailViewModel.selectedChapter?.prevChapterOrder == -1L || mangaDetailViewModel.selectedChapter?.prevChapterOrder == null).let {
            binding.ivPrevChapter.alpha =
                if (it) 0.3f else 1f
            binding.btnPrevChapter.isEnabled = !it
        }
        (mangaDetailViewModel.selectedChapter?.nextChapterOrder == -1L || mangaDetailViewModel.selectedChapter?.nextChapterOrder == null).let {
            binding.ivNextChapter.alpha =
                if (it)
                    0.3f else 1f
            binding.btnNextChapter.isEnabled = !it
        }
    }

    override fun setEventListeners() {
        super.setEventListeners()
        binding.tbHeader.setNavigationOnClickListener {
            viewModel.onBackPressed()
        }
        viewModel.isLoading.observe(this) {
            binding.pbLoading.isVisible = it
        }

        binding.llOptionIndex.setOnClickListener {
            mangaDetailViewModel.chapters.value?.let {
                ListIndexBottomSheetDialog.show(
                    childFragmentManager,
                    it
                ) { chapter ->
                    loadChapter(chapter)
                    setNavButtonStatus()
                }
                hideMenu()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setReaderMenuVisibility(menuVisible, animate = false)
    }

    override fun onDispatchKeyEvent(event: KeyEvent, dispatch: Boolean): Boolean {
        val handled = viewer.handleKeyEvent(event)
        return handled || dispatch
    }

    private fun initMenu() {
        initStatusBars()
        setReaderMenuVisibility(menuVisible)

        binding.btnPrevChapter.setOnClickListener {
            mangaDetailViewModel.selectedChapter?.prevChapterOrder?.let { index ->
                mangaDetailViewModel.chapters.value?.find { index == it.sourceOrder }
                    ?.let { chapter ->
                        loadChapter(chapter)
                    }
            }
            setNavButtonStatus()
        }

        binding.btnNextChapter.setOnClickListener {
            mangaDetailViewModel.selectedChapter?.nextChapterOrder?.let { index ->
                mangaDetailViewModel.chapters.value?.find { index == it.sourceOrder }
                    ?.let { chapter ->
                        loadChapter(chapter)
                    }
            }
            setNavButtonStatus()
        }
    }

    private fun loadChapter(chapter: ChapterEntity) {
        viewModel.getPages(chapter.url)
        binding.tvChapterName.text = chapter.name
        mangaDetailViewModel.selectedChapter = chapter
    }

    private fun initStatusBars() {
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun setReaderMenuVisibility(visible: Boolean, animate: Boolean = true) {
        menuVisible = visible
        if (visible) {
            binding.rlTopMenu.isVisible = true

            if (animate) {
                val toolbarAnimation = AnimationUtils.loadAnimation(context, R.anim.enter_from_top)
                toolbarAnimation.applySystemAnimatorScale(requireContext())
                toolbarAnimation.setAnimationListener(
                    object : SimpleAnimationListener() {
                        override fun onAnimationStart(animation: Animation) {
                            // Fix status bar being translucent the first time it's opened.
                            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        }
                    },
                )
                binding.appBarHeader.startAnimation(toolbarAnimation)

                val bottomAnimation =
                    AnimationUtils.loadAnimation(context, R.anim.enter_from_bottom)
                bottomAnimation.applySystemAnimatorScale(requireContext())
                binding.clBottomMenu.startAnimation(bottomAnimation)
            }
        } else {
            if (animate) {
                val toolbarAnimation = AnimationUtils.loadAnimation(context, R.anim.exit_to_top)
                toolbarAnimation.applySystemAnimatorScale(requireContext())
                toolbarAnimation.setAnimationListener(
                    object : SimpleAnimationListener() {
                        override fun onAnimationEnd(animation: Animation) {
                            binding.rlTopMenu.isVisible = false
                        }
                    },
                )
                binding.appBarHeader.startAnimation(toolbarAnimation)

                val bottomAnimation = AnimationUtils.loadAnimation(context, R.anim.exit_to_bottom)
                bottomAnimation.applySystemAnimatorScale(requireContext())
                binding.clBottomMenu.startAnimation(bottomAnimation)
            }
        }
    }

    fun toggleMenu() {
        setReaderMenuVisibility(!menuVisible)
    }

    fun showMenu() {
        if (!menuVisible) {
            setReaderMenuVisibility(true)
        }
    }

    fun hideMenu() {
        if (menuVisible) {
            setReaderMenuVisibility(false)
        }
    }
}