package com.selim.cryptomarket.ui.home

import androidx.compose.runtime.Composable
import com.selim.cryptomarket.ui.navigation.Navigation
import com.selim.cryptomarket.ui.theme.ThemeViewModel

@Composable
fun MainContent(themeViewModel: ThemeViewModel, isDarkTheme: Boolean) {
    Navigation(themeViewModel = themeViewModel, isDarkMode = isDarkTheme)
}
