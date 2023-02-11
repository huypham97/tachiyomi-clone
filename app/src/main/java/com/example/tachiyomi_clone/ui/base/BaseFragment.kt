package com.example.tachiyomi_clone.ui.base

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.common.event.EventObserver
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel> :
    DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    open val isViewModelProvideByActivity: Boolean = false

    abstract val viewModel: VM

    protected lateinit var binding: B

    protected val navController by lazy {
        findNavController()
    }

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
        setupPopBackStackEvent()
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
        onCreateBinding()
    }

    protected abstract fun getLayoutResId(): Int

    abstract fun getLifeCycleOwner(): LifecycleOwner

    open fun onCreateBinding() {}

    open fun initViews(savedInstanceState: Bundle?) {}

    open fun setEventListeners() {
        viewModel.isNetworkAvailable.observe(viewLifecycleOwner) { isAvailable ->
            if (!isAvailable) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.no_connect_internet_message_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    open fun onHandleBackPressed() {
        popBackStack(true)
    }

    open fun popBackStack(applyActivityPopBack: Boolean = false): Boolean {
        val check = navController.popBackStack()

        if (!check && applyActivityPopBack) {
            requireActivity().finish()
            return false
        }

        return check
    }

    open fun setupPopBackStackEvent() {
        viewModel.onBackPressedDispatcher.observe(viewLifecycleOwner, EventObserver {
            onHandleBackPressed()
        })
    }

    open fun onDispatchKeyEvent(event: KeyEvent, dispatch: Boolean): Boolean {
        return dispatch
    }
}