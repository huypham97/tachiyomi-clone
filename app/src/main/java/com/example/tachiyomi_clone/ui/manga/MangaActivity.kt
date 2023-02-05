package com.example.tachiyomi_clone.ui.manga

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.widget.VerticalSpacingDecoration
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.databinding.ActivityMangaBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity
import com.example.tachiyomi_clone.ui.reader.ReaderActivity
import com.example.tachiyomi_clone.utils.loadByHtml
import com.example.tachiyomi_clone.utils.setColor
import com.example.tachiyomi_clone.utils.system.ScreenUtils
import com.google.android.flexbox.FlexDirection.ROW
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import jp.wasabeef.glide.transformations.BlurTransformation

class MangaActivity : BaseActivity<ActivityMangaBinding, MangaViewModel>() {

    companion object {
        const val MANGA_ITEM = "MANGA_ITEM"
        private const val LOAD_MORE_PEAK = 50
    }

    //    private var mangaId: Long = -1L
    private var manga: MangaEntity? = null
    private val genreAdapter = MangaGenreAdapter()
    private val chapterAdapter = ChapterAdapter()
    private var count = 0
    private var isLoadingMore = false
    private var isShow = true
    private var scrollRange = -1

    override val modelClass: Class<MangaViewModel>
        get() = MangaViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_manga

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
//        this.mangaId = intent.getLongExtra(MANGA_URL, -1L)
        manga = intent.getSerializableExtra(MANGA_ITEM, MangaEntity::class.java)
//        viewModel.fetchDetailManga(mangaId)
        manga?.let {
            binding.tbHeader.title = it.title
            binding.tvMangaTitle.text = it.title
            viewModel.fetchMangaFromSource(it)
        }

        binding.rvMangaGenre.apply {
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = ROW
                justifyContent = JustifyContent.FLEX_START
            }
            adapter = genreAdapter
        }

        binding.rvChapter.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                VerticalSpacingDecoration(
                    this@MangaActivity,
                    R.drawable.divider
                )
            )
            adapter = chapterAdapter
        }

        binding.appBarHeader.addOnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.tbHeader.title = manga?.title
                isShow = true
            } else if (isShow) {
                binding.tbHeader.title =
                    " " //careful there should a space between double quote otherwise it wont work
                isShow = false
            }
        }

        ScreenUtils.getStatusBarHeight(this).let {
            binding.tbHeader.apply {
                val params = layoutParams as FrameLayout.LayoutParams
                params.setMargins(0, it, 0, 0)
            }
            binding.clContent.apply {
                val params = layoutParams as FrameLayout.LayoutParams
                params.setMargins(0, it + ScreenUtils.getAppBarHeight(this@MangaActivity), 0, 0)
            }
        }
    }

    override fun setEventListener() {
        super.setEventListener()
        binding.tbHeader.setNavigationOnClickListener {
            this.finish()
        }

        binding.tvReadFirst.setOnClickListener {
            viewModel.chapters.value?.let {
//                val intent = Intent(this, ReaderActivity::class.java)
//                intent.putExtra(ReaderActivity.MANGA_TITLE, binding.tvMangaTitle.text)
//                intent.putExtra(ReaderActivity.CHAPTER_URL, it[it.size - 1])
//                intent.putParcelableArrayListExtra(ReaderActivity.CHAPTER_LIST, ArrayList(it))
//                startActivity(intent)
                navigateToReaderActivity(it[it.size - 1].sourceOrder)
            }
        }

        binding.tvReadLast.setOnClickListener {
            viewModel.chapters.value?.let {
//                val intent = Intent(this, ReaderActivity::class.java)
//                intent.putExtra(ReaderActivity.MANGA_TITLE, binding.tvMangaTitle.text)
//                intent.putExtra(ReaderActivity.CHAPTER_URL, it[0])
//                startActivity(intent)
                navigateToReaderActivity(it[0].sourceOrder)
            }
        }

        viewModel.manga.observe(this) { manga ->
            Glide.with(this).asBitmap().apply(RequestOptions().apply {
                override(
                    binding.ivMangaThumbnail.width,
                    binding.ivMangaThumbnail.height
                )
            })
                .load(manga.thumbnailUrl)
                .into(binding.ivMangaThumbnail)
            Glide.with(this).asBitmap().apply(RequestOptions().apply {
                override(
                    binding.ivBackground.width,
                    binding.ivBackground.height
                )
            })
                .load(manga.thumbnailUrl)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                .into(binding.ivBackground)
            binding.tvMangaStatus.text = "${resources.getString(R.string.status)}: ${
                resources.getString(manga.getStatus()).setColor(this, R.color.color_FFB340)
            }".loadByHtml()
            binding.tvMangaAuthor.text = "${resources.getString(R.string.author)}: ${
                manga.author?.setColor(this, R.color.color_FFB340)
            }".loadByHtml()
            binding.etvMangaDescribe.setNoteContent(manga.description)
            manga.genre?.let { genreAdapter.refreshList(it) }
        }

        viewModel.chapters.observe(this) { chapters ->
            binding.tvChaptersCount.text = "Đã ra ${chapters.size} chương"
            setChapterList(chapters)
        }

        viewModel.isLoading.observe(this) {
            binding.pbLoading.isVisible = it
        }

        chapterAdapter.onSelectChapterListener = {
//            val intent = Intent(this, ReaderActivity::class.java)
//            intent.putExtra(ReaderActivity.MANGA_TITLE, binding.tvMangaTitle.text)
//            intent.putExtra(ReaderActivity.CHAPTER_URL, it)
//            startActivity(intent)
            navigateToReaderActivity(it.sourceOrder)
        }

        binding.ivChapterSort.setOnClickListener {
            count = 0
            viewModel.sortChapterList()
            binding.tvChapterSortTitle.text =
                resources.getText(if (viewModel.ascendingSort) R.string.newest else R.string.oldest)
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

    private fun navigateToReaderActivity(id: Long) {
        val intent = Intent(this, ReaderActivity::class.java)
        intent.putExtra(ReaderActivity.MANGA_TITLE, binding.tvMangaTitle.text)
        intent.putExtra(ReaderActivity.CHAPTER_SELECTED_ID, id)
        intent.putParcelableArrayListExtra(ReaderActivity.CHAPTER_LIST,
            viewModel.chapters.value?.let { ArrayList(it) })
        startActivity(intent)
    }
}