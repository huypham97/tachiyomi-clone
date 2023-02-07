package com.example.tachiyomi_clone.ui.base

import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.fragment.findNavController
import kotlin.reflect.KClass

abstract class BaseNavFragment<B : ViewDataBinding, VM : BaseViewModel>(
    viewModelClass: KClass<VM>,
    @IdRes navGraphId: Int
) :
    BaseFragment<B, VM>() {

    override val viewModel: VM by viewModelNavGraph(
        navGraphId = navGraphId,
        clazz = viewModelClass,
        factoryProducer = { viewModelFactory })

    protected fun Fragment.viewModelNavGraph(
        @IdRes navGraphId: Int,
        clazz: KClass<VM>,
        extrasProducer: (() -> CreationExtras)? = null,
        factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<VM> {
        val backStackEntry by lazy {
            findNavController().getBackStackEntry(navGraphId)
        }
        val storeProducer: () -> ViewModelStore = {
            backStackEntry.viewModelStore
        }
        return createViewModelLazy(
            clazz, storeProducer,
            { extrasProducer?.invoke() ?: backStackEntry.defaultViewModelCreationExtras },
            factoryProducer ?: { backStackEntry.defaultViewModelProviderFactory }
        )
    }

}