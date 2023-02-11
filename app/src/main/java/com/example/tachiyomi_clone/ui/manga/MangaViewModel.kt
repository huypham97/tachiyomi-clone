package com.example.tachiyomi_clone.ui.manga

import com.example.tachiyomi_clone.data.network.common.ConnectionHelper
import com.example.tachiyomi_clone.ui.base.BaseViewModel
import javax.inject.Inject

class MangaViewModel @Inject constructor(
    private val connectionHelper: ConnectionHelper
) : BaseViewModel(connectionHelper) {
}