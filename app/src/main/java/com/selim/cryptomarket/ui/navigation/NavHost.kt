package com.selim.cryptomarket.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.selim.cryptomarket.ui.home.HomeScreen
import com.selim.cryptomarket.ui.home.SettingsScreen
import com.selim.cryptomarket.ui.home.ThemeViewModel
import com.selim.cryptomarket.ui.navigation.Screen.Home
import com.selim.cryptomarket.ui.navigation.Screen.Search
import com.selim.cryptomarket.ui.navigation.Screen.Settings
import com.selim.cryptomarket.ui.search.SearchScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    startDestination: String = Home.route,
    themeViewModel: ThemeViewModel,
    isDarkMode: Boolean
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Home.route) {
            HomeScreen(navController, themeViewModel, isDarkMode)
        }
        composable(route = Settings.route) {
            SettingsScreen(navController)
        }
        composable(route = Search.route) {
            SearchScreen(navController)
        }
    }
}
