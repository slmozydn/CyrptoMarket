package com.selim.cryptomarket.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Settings : Screen("settings")
    object Search : Screen("search")
    object Detail : Screen("detail/{id}") {
        val arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
            }
        )
    }
}
