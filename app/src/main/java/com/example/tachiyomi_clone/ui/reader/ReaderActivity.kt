package com.example.tachiyomi_clone.ui.reader

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.listener.SimpleAnimationListener
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.databinding.ActivityReaderBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity
import com.example.tachiyomi_clone.ui.reader.webtoon.WebtoonViewer
import com.example.tachiyomi_clone.utils.system.applySystemAnimatorScale

class ReaderActivity : BaseActivity<ActivityReaderBinding, ReaderViewModel>() {

    companion object {
        const val CHAPTER_SELECTED_ID = "CHAPTER_SELECTED_ID"
        const val CHAPTER_LIST = "CHAPTER_LIST"
        const val MANGA_TITLE = "MANGA_TITLE"
    }

    private var chapterId: Long? = null
    private var mangaTitle: String? = null
    private var chapterSelected: ChapterEntity? = null
    private var listChapter: List<ChapterEntity>? = null
    private lateinit var viewer: WebtoonViewer
    var menuVisible = false
        private set
    private val windowInsetsController by lazy {
        WindowInsetsControllerCompat(
            window,
            binding.root
        )
    }

    override val modelClass: Class<ReaderViewModel>
        get() = ReaderViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_reader

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        initializedIntent()
        viewer = WebtoonViewer(this)
        binding.tbHeader.title = mangaTitle
        binding.tvChapterName.text = chapterSelected?.name
        viewModel.getPages(chapterSelected?.url ?: "")
        binding.flPage.addView(viewer.getView())
        binding.tbHeader.menu.getItem(0).isVisible = false

        setNavButtonStatus()

        initMenu()
    }

    private fun setNavButtonStatus() {
        (chapterSelected?.prevChapterOrder == -1L || chapterSelected?.prevChapterOrder == null).let {
            binding.ivPrevChapter.alpha =
                if (it) 0.3f else 1f
            binding.btnPrevChapter.isEnabled = !it
        }
        (chapterSelected?.nextChapterOrder == -1L || chapterSelected?.nextChapterOrder == null).let {
            binding.ivNextChapter.alpha =
                if (it)
                    0.3f else 1f
            binding.btnNextChapter.isEnabled = !it
        }
    }

    private fun initializedIntent() {
        mangaTitle = intent.getStringExtra(MANGA_TITLE)
        chapterId = intent.getLongExtra(CHAPTER_SELECTED_ID, -1)
        listChapter = intent.getParcelableArrayListExtra(CHAPTER_LIST, ChapterEntity::class.java)
        chapterSelected = listChapter?.find { chapterId == it.sourceOrder }
    }

    override fun setEventListener() {
        super.setEventListener()
        binding.tbHeader.setNavigationOnClickListener {
            this.finish()
        }
        viewModel.isLoading.observe(this) {
            binding.pbLoading.isVisible = it
        }
    }

    override fun onResume() {
        super.onResume()
        setMenuVisibility(menuVisible, animate = false)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus)
            setMenuVisibility(menuVisible, animate = false)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val handled = viewer.handleKeyEvent(event)
        return handled || super.dispatchKeyEvent(event)
    }

    private fun initMenu() {
        initStatusBars()
        setMenuVisibility(menuVisible)

        binding.btnPrevChapter.setOnClickListener {
            chapterSelected?.prevChapterOrder?.let { index ->
                listChapter?.find { index == it.sourceOrder }?.let { chapter ->
                    viewModel.getPages(chapter.url)
                    binding.tvChapterName.text = chapter.name
                    chapterId = chapter.sourceOrder
                    chapterSelected = chapter
                }
            }
            setNavButtonStatus()
        }

        binding.btnNextChapter.setOnClickListener {
            chapterSelected?.nextChapterOrder?.let { index ->
                listChapter?.find { index == it.sourceOrder }?.let { chapter ->
                    viewModel.getPages(chapter.url)
                    binding.tvChapterName.text = chapter.name
                    chapterId = chapter.sourceOrder
                    chapterSelected = chapter
                }
            }
            setNavButtonStatus()
        }
    }

    private fun initStatusBars() {
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun setMenuVisibility(visible: Boolean, animate: Boolean = true) {
        menuVisible = visible
        if (visible) {
            binding.rlTopMenu.isVisible = true

            if (animate) {
                val toolbarAnimation = AnimationUtils.loadAnimation(this, R.anim.enter_from_top)
                toolbarAnimation.applySystemAnimatorScale(this)
                toolbarAnimation.setAnimationListener(
                    object : SimpleAnimationListener() {
                        override fun onAnimationStart(animation: Animation) {
                            // Fix status bar being translucent the first time it's opened.
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        }
                    },
                )
                binding.appBarHeader.startAnimation(toolbarAnimation)

                val bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom)
                bottomAnimation.applySystemAnimatorScale(this)
                binding.clBottomMenu.startAnimation(bottomAnimation)
            }
        } else {
            if (animate) {
                val toolbarAnimation = AnimationUtils.loadAnimation(this, R.anim.exit_to_top)
                toolbarAnimation.applySystemAnimatorScale(this)
                toolbarAnimation.setAnimationListener(
                    object : SimpleAnimationListener() {
                        override fun onAnimationEnd(animation: Animation) {
                            binding.rlTopMenu.isVisible = false
                        }
                    },
                )
                binding.appBarHeader.startAnimation(toolbarAnimation)

                val bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.exit_to_bottom)
                bottomAnimation.applySystemAnimatorScale(this)
                binding.clBottomMenu.startAnimation(bottomAnimation)
            }
        }
    }

    fun toggleMenu() {
        setMenuVisibility(!menuVisible)
    }

    fun showMenu() {
        if (!menuVisible) {
            setMenuVisibility(true)
        }
    }

    fun hideMenu() {
        if (menuVisible) {
            setMenuVisibility(false)
        }
    }

}