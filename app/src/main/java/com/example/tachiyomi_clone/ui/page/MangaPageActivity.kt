package com.example.tachiyomi_clone.ui.page

import android.os.Build
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.MangasPageEntity
import com.example.tachiyomi_clone.databinding.ActivityMangaPageBinding
import com.example.tachiyomi_clone.ui.base.BaseActivity

class MangaPageActivity : BaseActivity<ActivityMangaPageBinding, MangaPageViewModel>() {

    companion object {
        const val MODULE_ITEM = "MODULE_ITEM"
    }

    private var module: MangasPageEntity? = null

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
    }

    private fun showMangaPageFragment() {
        supportFragmentManager.beginTransaction().let {
            val bundle = Bundle()
            bundle.putSerializable(MODULE_ITEM, module)
            val fragment = NavHostFragment.create(R.navigation.nav_graph_page, bundle)
            it.replace(
                binding.flContainer.id,
                fragment
            )
            it.commit()
        }
    }
}