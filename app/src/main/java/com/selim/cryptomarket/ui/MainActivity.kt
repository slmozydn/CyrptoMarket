package com.selim.cryptomarket.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import com.selim.cryptomarket.databinding.ActivityMainBinding
import com.selim.cryptomarket.ui.home.MainContent
import com.selim.cryptomarket.ui.home.ThemeViewModel
import com.selim.cryptomarket.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var binding: ActivityMainBinding
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
