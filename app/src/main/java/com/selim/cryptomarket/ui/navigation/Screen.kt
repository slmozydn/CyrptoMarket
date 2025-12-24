package com.selim.cryptomarket.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object Settings : Screen()

    @Serializable
    data object Search : Screen()

    @Serializable
    data class Detail(val coinId: String) : Screen()
}
