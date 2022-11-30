package com.example.tachiyomi_clone.ui.main.base

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(), Observable {

    var dataBindingCallback = PropertyChangeRegistry()

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