package com.example.tachiyomi_clone.ui.main

import com.example.tachiyomi_clone.data.network.common.ConnectionHelper
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private val connectionHelper: ConnectionHelper) :
    BaseViewModel(connectionHelper) {

    companion object {
        const val TAG = "MainViewModel"
    }

}