package com.example.tachiyomi_clone.ui.page

import android.os.Build
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.GenreEntity
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.databinding.ActivityMangaPageBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity

class MangaPageActivity : BaseActivity<ActivityMangaPageBinding, MangaPageViewModel>() {

    companion object {
        const val MODULE_ITEM = "MODULE_ITEM"
        const val MANGA_GENRE = "MANGA_GENRE"
    }

    private var module: MangasPageEntity? = null
    private var genre: GenreEntity? = null

    override val modelClass: Class<MangaPageViewModel>
        get() = MangaPageViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_manga_page

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        initData()
        showMangaPageFragment()
    }

    private fun initData() {
        module = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(MODULE_ITEM, MangasPageEntity::class.java)
        } else {
            intent.getSerializableExtra(MODULE_ITEM) as MangasPageEntity
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            genre = intent.getSerializableExtra(MANGA_GENRE, GenreEntity::class.java)
        } else {
            genre = intent.getSerializableExtra(MANGA_GENRE) as GenreEntity
        }
    }

    private fun showMangaPageFragment() {
        supportFragmentManager.beginTransaction().let {
            val bundle = Bundle()
            module?.let { data ->
                bundle.putSerializable(MODULE_ITEM, data)
            }
            genre?.let { data ->
                bundle.putSerializable(MANGA_GENRE, data)
            }
            val fragment = NavHostFragment.create(R.navigation.nav_graph_page, bundle)
            it.replace(
                binding.flContainer.id,
                fragment
            )
            it.commit()
        }
    }
}