package com.selim.cryptomarket.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.selim.cryptomarket.ui.detail.DetailScreen
import com.selim.cryptomarket.ui.home.HomeScreen
import com.selim.cryptomarket.ui.settings.SettingsScreen
import com.selim.cryptomarket.ui.theme.ThemeViewModel
import com.selim.cryptomarket.ui.search.SearchScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel,
    isDarkMode: Boolean,
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Home,
    ) {
        composable<Screen.Home> {
            HomeScreen(navController, themeViewModel, isDarkMode)
        }

        composable<Screen.Settings> {
            SettingsScreen(navController)
        }

        composable<Screen.Search> {
            SearchScreen(navController)
        }

        composable<Screen.Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Screen.Detail>()
            DetailScreen(coinId = detail.coinId)
        }
    }
}
