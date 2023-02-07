package com.example.tachiyomi_clone.ui.main.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.widget.SpaceItemDecoration
import com.example.tachiyomi_clone.databinding.FragmentSearchBinding
import com.example.tachiyomi_clone.ui.base.BaseGeneralFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment :
    BaseGeneralFragment<FragmentSearchBinding, SearchViewModel>(SearchViewModel::class) {

    companion object {
        const val TAG = "SearchFragment"

        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    private val searchAdapter = SearchAdapter()

    override fun getLayoutResId(): Int = R.layout.fragment_search

    override fun getLifeCycleOwner(): LifecycleOwner = this

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        this.binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                SpaceItemDecoration(
                    mSpace = context.resources.getDimension(R.dimen.et_dimen_20).toInt()
                )
            )
            adapter = searchAdapter
        }
    }

    override fun setEventListeners() {
        super.setEventListeners()
        lifecycleScope.launch {
            binding.llInput.getTextInputEditText().getQueryTextChangeStateFlow().debounce(300)
                .filter { query ->
                    if (query.isEmpty()) {
                        withContext(Dispatchers.Main) {
                            searchAdapter.refresh()
                        }
                        return@filter false
                    } else {
                        return@filter true
                    }
                }.distinctUntilChanged()
                .flatMapLatest { query ->
                    binding.pbLoading.isVisible = true
                    viewModel.fetchSearchManga(query).catch { emitAll(flowOf(PagingData.empty())) }
                }
                .collectLatest { data ->
                    binding.pbLoading.isVisible = false
                    searchAdapter.submitData(data)
                }
        }
    }

    private fun TextInputEditText.getQueryTextChangeStateFlow(): StateFlow<String> {
        val query = MutableStateFlow("")

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // beforeTextChanged
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                query.value = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                // afterTextChanged
            }
        })
        return query
    }
}