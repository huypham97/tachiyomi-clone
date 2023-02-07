package com.example.tachiyomi_clone.ui.main.search

import androidx.lifecycle.LifecycleOwner
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.FragmentSearchBinding
import com.example.tachiyomi_clone.ui.base.BaseGeneralFragment

class SearchFragment :
    BaseGeneralFragment<FragmentSearchBinding, SearchViewModel>(SearchViewModel::class) {

    companion object {
        const val TAG = "SearchFragment"

        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    override fun getLayoutResId(): Int = R.layout.fragment_search

    override fun getLifeCycleOwner(): LifecycleOwner = this
}