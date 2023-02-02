package com.example.tachiyomi_clone.ui.main.home

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.data.model.entity.MangaEntity
import com.example.tachiyomi_clone.databinding.FragmentHomeBinding
import com.example.tachiyomi_clone.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    companion object {
        const val TAG = "HomeFragment"

        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    private lateinit var bannerAdapter: BannerAdapter

    override val modelClass: Class<HomeViewModel>
        get() = HomeViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun getLifeCycleOwner(): LifecycleOwner = this

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        bannerAdapter = BannerAdapter()
        bannerAdapter.refreshList(
            listOf(
                MangaEntity.create()
                    .copy(thumbnailUrl = "https://bvhttdl.mediacdn.vn/291773308735864832/2021/3/16/photo-1-1615870720601745881145-1615882898863-16158828989761108660908-1615883538504-1615883539327865048813.jpg"),
                MangaEntity.create()
                    .copy(thumbnailUrl = "https://i.guim.co.uk/img/media/2281074279af96115bbfdfa2f64dfc1eab685d69/0_0_3000_2318/master/3000.jpg?width=1300&quality=85&dpr=1&s=none"),
                MangaEntity.create()
                    .copy(thumbnailUrl = "https://bvhttdl.mediacdn.vn/291773308735864832/2021/3/16/photo-1-1615870720601745881145-1615882898863-16158828989761108660908-1615883538504-1615883539327865048813.jpg"),
                MangaEntity.create()
                    .copy(thumbnailUrl = "https://i.guim.co.uk/img/media/c64cbcd6e90b9ef454bdaf2f075a6eec820b4d20/0_0_1594_2244/master/1594.jpg?width=380&quality=85&dpr=1&s=none"),
            )
        )
        binding.vpBanner.adapter = bannerAdapter
    }
}