package com.example.tachiyomi_clone.ui.manga

import android.os.Build
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.databinding.ActivityMangaBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity

class MangaActivity : BaseActivity<ActivityMangaBinding, MangaViewModel>() {

    companion object {
        const val MANGA_ITEM = "MANGA_ITEM"
    }

    //    private var mangaId: Long = -1L
    private var manga: MangaEntity? = null

    override val modelClass: Class<MangaViewModel>
        get() = MangaViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_manga

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        manga = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(MANGA_ITEM, MangaEntity::class.java)
        } else {
            intent.getSerializableExtra(MANGA_ITEM) as MangaEntity
        }
        showMangaFragment()
    }

    private fun showMangaFragment() {
        supportFragmentManager.beginTransaction().let {
            val bundle = Bundle()
            bundle.putSerializable(MANGA_ITEM, manga)
            val fragment = NavHostFragment.create(R.navigation.nav_graph_manga, bundle)
            it.replace(
                binding.flContainer.id,
                fragment
            )
            it.commit()
        }
    }
}