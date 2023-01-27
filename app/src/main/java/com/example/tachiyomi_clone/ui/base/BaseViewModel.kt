package com.example.tachiyomi_clone.ui.base

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(), Observable {

    var dataBindingCallback = PropertyChangeRegistry()

    var isLoading = MutableLiveData<Boolean>()

    protected fun showLoadingDialog() {
        isLoading.value = true
    }

    protected fun dismissLoadingDialog() {
        isLoading.value = false
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        dataBindingCallback.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        dataBindingCallback.remove(callback)
    }

    protected fun notifyPropertyChanged(fieldId: Int) {
        dataBindingCallback.notifyCallbacks(this, fieldId, null)
    }

}