package com.selim.cryptomarket.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Search : Screen("search")
    object Detail : Screen("detail")
}
