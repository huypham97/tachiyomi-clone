package com.example.tachiyomi_clone.ui.page

import com.example.tachiyomi_clone.data.network.common.ConnectionHelper
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import javax.inject.Inject

class MangaPageViewModel @Inject constructor(private val connectionHelper: ConnectionHelper) :
    BaseViewModel(connectionHelper) {
}