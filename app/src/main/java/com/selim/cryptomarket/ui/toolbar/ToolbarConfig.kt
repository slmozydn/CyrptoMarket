package com.selim.cryptomarket.ui.toolbar

import androidx.annotation.StringRes
import com.selim.cryptomarket.R

data class ToolbarConfig(
    @StringRes val titleRes: Int? = null,
    @StringRes val searchHintRes: Int = R.string.search,
    val cancelTextVisible: Boolean = false,
    val searchVisible: Boolean = false,
    val settingsVisible: Boolean = false,
    val themeVisible: Boolean = false,
    val backButtonVisible: Boolean = false
)
