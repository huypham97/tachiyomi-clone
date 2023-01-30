package com.example.tachiyomi_clone.ui.manga

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.ActivityMangaBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity
import com.example.tachiyomi_clone.ui.reader.ReaderActivity
import com.google.android.flexbox.FlexDirection.ROW
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class MangaActivity : BaseActivity<ActivityMangaBinding, MangaViewModel>() {

    companion object {
        const val MANGA_ID = "MANGA_ID"
    }

    private var mangaId: Long = -1L
    private val genreAdapter = MangaGenreAdapter()
    private val chapterAdapter = ChapterAdapter()
    private var count = 0
    private var isLoading = false
    private val handler = Handler()

    override val modelClass: Class<MangaViewModel>
        get() = MangaViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_manga

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        this.mangaId = intent.getLongExtra(MANGA_ID, -1L)
        viewModel.fetchDetailManga(mangaId)
        binding.rvMangaGenre.apply {
            layoutManager = FlexboxLayoutManager(context).apply {
                flexDirection = ROW
                justifyContent = JustifyContent.FLEX_START
            }
            adapter = genreAdapter
        }
        binding.rvChapter.apply {
            layoutManager = LinearLayoutManager(context).apply {
                isAutoMeasureEnabled = true
            }
            adapter = chapterAdapter
        }
    }

    override fun setEventListener() {
        super.setEventListener()
        binding.tbHeader.setNavigationOnClickListener {
            this.finish()
        }
        viewModel.manga.observe(this) { manga ->
            Glide.with(this).load(manga.thumbnailUrl)
                .into(binding.ivMangaThumbnail)
            if (manga.title.isNotEmpty()) binding.tvMangaTitle.text = manga.title
            binding.tvMangaStatus.text = "Status: ${resources.getString(manga.getStatus())}"
            binding.tvMangaAuthor.text = "Author: ${manga.author}"
            binding.tvMangaDescribe.text = manga.description
            manga.genre?.let { genreAdapter.refreshList(it) }
            if (manga.title.isNotEmpty()) binding.tbHeader.title = manga.title
        }
        viewModel.chapters.observe(this) { chapters ->
            binding.tvChaptersCount.text = "${chapters.size} chapters"
            count = if (chapters.size > 50) 50 else chapters.size
            chapterAdapter.refreshList(chapters.take(count))
        }
        viewModel.isLoading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }
        chapterAdapter.onSelectChapterListener = {
            val intent = Intent(this, ReaderActivity::class.java)
            intent.putExtra(ReaderActivity.CHAPTER_URL, it)
            startActivity(intent)
        }
        initScrollListener()
    }

    private fun initScrollListener() {
        binding.nsv.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                if (!isLoading) {
                    loadMore()
                    isLoading = true
                }
            }
        })
    }

    private fun loadMore() {
        handler.postDelayed({
            isLoading = false
            count += if (viewModel.chapters.value!!.size - count > 50) 50 else viewModel.chapters.value!!.size - count
            chapterAdapter.refreshList(viewModel.chapters.value!!.take(count))
        }, 100)
    }
}