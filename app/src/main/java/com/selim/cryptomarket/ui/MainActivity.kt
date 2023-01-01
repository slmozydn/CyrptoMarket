package com.selim.cryptomarket.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import com.selim.cryptomarket.ui.home.MainContent
import com.selim.cryptomarket.ui.theme.ThemeViewModel
import com.selim.cryptomarket.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemTheme = isSystemInDarkTheme()
            val isDarkTheme = themeViewModel.isDarkThemeEnabled.collectAsState(initial = systemTheme).value

            AppTheme(isDarkTheme = isDarkTheme) {
                MainContent(themeViewModel, isDarkTheme)
            }
        }
    }
}
