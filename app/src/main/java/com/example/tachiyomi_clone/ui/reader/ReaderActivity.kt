package com.example.tachiyomi_clone.ui.reader

import android.os.Bundle
import android.view.View
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.ChapterEntity
import com.example.tachiyomi_clone.databinding.ActivityReaderBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity
import com.example.tachiyomi_clone.ui.reader.webtoon.WebtoonViewer

class ReaderActivity : BaseActivity<ActivityReaderBinding, ReaderViewModel>() {

    companion object {
        const val CHAPTER_URL = "CHAPTER_URL"
        const val MANGA_TITLE = "MANGA_TITLE"
    }

    private var chapter: ChapterEntity? = null
    private var mangaTitle: String? = null

    private lateinit var viewer: WebtoonViewer

    override val modelClass: Class<ReaderViewModel>
        get() = ReaderViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_reader

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        mangaTitle = intent.getStringExtra(MANGA_TITLE)
        chapter = intent.getSerializableExtra(CHAPTER_URL, ChapterEntity::class.java)
        viewer = WebtoonViewer(this)
        binding.tbHeader.title = mangaTitle
        binding.tbHeader.subtitle = chapter?.name
        viewModel.getPages(chapter?.url ?: "")
        binding.flPage.addView(viewer.getView())
    }

    override fun setEventListener() {
        super.setEventListener()
        binding.tbHeader.setNavigationOnClickListener {
            this.finish()
        }
        viewModel.isLoading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

}