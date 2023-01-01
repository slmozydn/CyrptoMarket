package com.selim.cryptomarket.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val LightTypography = Typography(
    subtitle1 = Typography().subtitle1.copy(
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontSize = 14.sp
    ),

    subtitle2 = Typography().subtitle2.copy(
        fontWeight = FontWeight.Normal,
        color = Color.Black,
        fontSize = 14.sp
    ),

    caption = Typography().caption.copy(
        fontWeight = FontWeight.Light,
        color = Color.Gray,
        fontSize = 12.sp
    )
)

val DarkTypography = Typography(
    subtitle1 = Typography().subtitle1.copy(
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 14.sp
    ),

    subtitle2 = Typography().subtitle2.copy(
        fontWeight = FontWeight.Normal,
        color = Color.White,
        fontSize = 14.sp
    ),

    caption = Typography().caption.copy(
        fontWeight = FontWeight.Light,
        color = Color.LightGray,
        fontSize = 12.sp
    )
)
