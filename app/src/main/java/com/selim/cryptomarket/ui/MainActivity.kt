package com.selim.cryptomarket.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.selim.cryptomarket.databinding.ActivityMainBinding
import com.selim.cryptomarket.ui.home.MainContent
import com.selim.cryptomarket.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemTheme = isSystemInDarkTheme()
            val isDarkTheme = remember { mutableStateOf(systemTheme) }

            AppTheme(isDarkTheme = false) {
                MainContent(isDarkTheme = isDarkTheme)
            }
        }
    }
}
