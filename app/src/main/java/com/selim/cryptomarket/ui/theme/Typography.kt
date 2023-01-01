package com.selim.cryptomarket.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val LightTypography = Typography(
    h1 = Typography().h3.copy(
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontSize = 28.sp,
        letterSpacing = 1.002.sp
    ),

    h2 = Typography().h3.copy(
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontSize = 18.sp,
        letterSpacing = 1.002.sp
    ),

    h3 = Typography().h3.copy(
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontSize = 16.sp,
        letterSpacing = 1.002.sp
    ),

    subtitle1 = Typography().subtitle1.copy(
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontSize = 14.sp,
        letterSpacing = 1.002.sp
    ),

    subtitle2 = Typography().subtitle2.copy(
        fontWeight = FontWeight.Normal,
        color = Color.Black,
        fontSize = 14.sp,
        letterSpacing = 1.002.sp
    ),

    caption = Typography().caption.copy(
        fontWeight = FontWeight.Normal,
        color = Color(0xFF888D91),
        fontSize = 12.sp,
        letterSpacing = 1.002.sp
    )
)

val DarkTypography = Typography(
    h1 = Typography().h3.copy(
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 20.sp,
        letterSpacing = 1.002.sp
    ),

    h2 = Typography().h3.copy(
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 18.sp,
        letterSpacing = 1.002.sp
    ),

    h3 = Typography().h3.copy(
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 16.sp,
        letterSpacing = 1.002.sp
    ),

    subtitle1 = Typography().subtitle1.copy(
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 14.sp,
        letterSpacing = 1.002.sp
    ),

    subtitle2 = Typography().subtitle2.copy(
        fontWeight = FontWeight.Normal,
        color = Color.White,
        fontSize = 14.sp,
        letterSpacing = 1.002.sp
    ),

    caption = Typography().caption.copy(
        fontWeight = FontWeight.Normal,
        color = Color(0xFF888D91),
        fontSize = 12.sp,
        letterSpacing = 1.002.sp
    )
)
