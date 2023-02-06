package com.example.tachiyomi_clone.ui.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.reflect.KClass

abstract class BaseGeneralFragment<B : ViewDataBinding, VM : BaseViewModel>(viewModelClass: KClass<VM>) :
    BaseFragment<B, VM>() {

    override val viewModel by viewModel(viewModelClass) { viewModelFactory }

    private fun Fragment.viewModel(
        clazz: KClass<VM>,
        ownerProducer: () -> ViewModelStoreOwner = { this },
        factoryProducer: (() -> ViewModelProvider.Factory)? = null,
    ) = createViewModelLazy(
        viewModelClass = clazz,
        storeProducer = { ownerProducer().viewModelStore },
        factoryProducer = factoryProducer
    )
}