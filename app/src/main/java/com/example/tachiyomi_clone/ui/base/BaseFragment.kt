package com.example.tachiyomi_clone.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    abstract val modelClass: Class<VM>

    open val isViewModelProvideByActivity: Boolean = false

    protected val viewModel by lazy {
        ViewModelProvider(
            if (isViewModelProvideByActivity) {
                requireActivity()
            } else {
                this
            }, viewModelFactory
        ).get(modelClass)
    }

    protected lateinit var binding: B

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setContentView(inflater, getLayoutResId(), container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(savedInstanceState)
        setEventListeners()
    }

    protected open fun setContentView(
        inflater: LayoutInflater,
        layoutResID: Int,
        container: ViewGroup?
    ) {
        binding = DataBindingUtil.inflate(inflater, layoutResID, container, false)
        binding.lifecycleOwner = getLifeCycleOwner()
//        binding.setVariable(BR.viewModel, viewModel)
        onCreateBinding()
    }

    protected abstract fun getLayoutResId(): Int

    abstract fun getLifeCycleOwner(): LifecycleOwner

    open fun onCreateBinding() {}

    open fun initViews(savedInstanceState: Bundle?) {}

    open fun setEventListeners() {}

}