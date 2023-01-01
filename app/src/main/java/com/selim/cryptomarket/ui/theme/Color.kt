package com.selim.cryptomarket.ui.theme

import android.annotation.SuppressLint
import android.provider.CalendarContract.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
val LightThemeColors = lightColors(
    primary = Color(0xFFF0B90B),
    primaryVariant = Color.Cyan, // boş
    secondary = Color(0xFFBAC3D0),
    secondaryVariant = Color(0xFFEBECEF),
    background = Color(0xFFF5F5F5),
    surface = Color.Red, // boş
    error = Color.DarkGray, // boş
    onPrimary = Color(0xFF2FBE85),
    onSecondary = Color(0xFFF6455D),
    onBackground = Color.White,
    onSurface = Color(0xFF8F9BA7),
    onError = Color(0xFFCF6679) // boş
)

val DarkThemeColors = darkColors(
    primary = Color(0xFFE50914),
    primaryVariant = Color(0xFF971C1C),
    secondary = Color(0xFFE50914),
    secondaryVariant = Color(0xFF831010),
    background = Color(0xFF1C1C1C),
    error = Color(0xFFCF6679),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color(0xFF1C1C1C)
)
