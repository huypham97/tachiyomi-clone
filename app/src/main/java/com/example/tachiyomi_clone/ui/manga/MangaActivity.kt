package com.example.tachiyomi_clone.ui.manga

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.ActivityMangaBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity
import com.example.tachiyomi_clone.utils.leftDrawable
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
            layoutManager = LinearLayoutManager(context)
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
            binding.tvMangaTitle.text = manga.title
            binding.tvMangaStatus.apply {
                setText(manga.getStatus())
                leftDrawable()
            }
            binding.tvMangaDescribe.text = manga.description
            manga.genre?.let { genreAdapter.refreshList(it) }
        }
        viewModel.chapters.observe(this) { chapters ->
            binding.tvChaptersCount.text = "${chapters.size} chapters"
            chapterAdapter.refreshList(chapters)
        }
        viewModel.isLoading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

}