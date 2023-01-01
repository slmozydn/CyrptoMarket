package com.selim.cryptomarket.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

// use material 3
val LightThemeColors = lightColors(
    primary = Color(0xFFF0B90B),
    primaryVariant = Color(0xFF888D91),
    secondary = Color(0xFFBAC3D0),
    secondaryVariant = Color(0xFFEBECEF),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFD5A50D),
    error = Color.DarkGray, // boş
    onPrimary = Color(0xFF2FBE85), // boş
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color(0xFF8F9BA7),
    onError = Color(0xFFCF6679) // boş
)

val DarkThemeColors = darkColors(
    primary = Color(0xFFF0B90B),
    primaryVariant = Color(0xFF888D91),
    secondary = Color(0xFFBAC3D0),
    secondaryVariant = Color(0xFF343B46),
    background = Color(0xFF181E26),
    surface = Color(0xFFD5A50D),
    error = Color.DarkGray, // boş
    onPrimary = Color(0xFF2FBE85), // boş
    onSecondary = Color.White,
    onBackground = Color(0xFF202630),
    onSurface = Color(0xFF8F9BA7),
    onError = Color(0xFF1C1C1C) //boş
)
