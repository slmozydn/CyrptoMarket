package com.selim.cryptomarket.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(isDarkTheme: Boolean = false, content: @Composable () -> Unit) {
    val typography = if (isDarkTheme) DarkTypography else LightTypography
    val colors = if (isDarkTheme) DarkThemeColors else LightThemeColors
    MaterialTheme(colors = colors, content = content, typography = typography)
}
