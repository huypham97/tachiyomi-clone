package com.example.tachiyomi_clone.ui.manga

import android.os.Bundle
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.ActivityMangaBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity

class MangaActivity : BaseActivity<ActivityMangaBinding, MangaViewModel>() {

    private var mangaId: Long = -1L

    companion object {
        const val MANGA_ID = "MANGA_ID"
    }

    override val modelClass: Class<MangaViewModel>
        get() = MangaViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_manga

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        this.mangaId = intent.getLongExtra(MANGA_ID, -1L)
        viewModel.fetchDetailManga(mangaId)
    }

    override fun setEventListener() {
        super.setEventListener()
        binding.tbHeader.setNavigationOnClickListener {
            this.finish()
        }
    }

}