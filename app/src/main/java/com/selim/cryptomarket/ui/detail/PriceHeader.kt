package com.selim.cryptomarket.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage

@Composable
fun PriceHeader(
    modifier: Modifier = Modifier,
    currency: String,
    icon: String,
    price: String,
    changeRate: String,
    isChangeRatePositive: Boolean,
) {
    ConstraintLayout(modifier = modifier) {
        val (coinIcon, textCurrency, textPrice, imageChangeRate, textChangeRate) = createRefs()
        AsyncImage(model = icon, contentDescription = null, modifier = Modifier.constrainAs(coinIcon) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
        })
        Text(
            text = currency,
            style = typography.subtitle2,
            modifier = Modifier.constrainAs(textCurrency) {
                start.linkTo(coinIcon.end)
                top.linkTo(parent.top)
            })

        Text(text = price, style = typography.h1, modifier = Modifier
            .constrainAs(textPrice) {
                start.linkTo(parent.start)
                top.linkTo(textCurrency.bottom)
            }
            .padding(top = 4.dp))

        Icon(
            imageVector = if (isChangeRatePositive) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            tint = if (isChangeRatePositive) Color(0xFF2FBE85) else Color(0xFFF6455D),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(imageChangeRate) {
                    top.linkTo(textChangeRate.top)
                    bottom.linkTo(textChangeRate.bottom)
                    end.linkTo(textChangeRate.start)
                    height = Dimension.fillToConstraints
                }
        )

        Text(
            text = changeRate,
            color = if (isChangeRatePositive) Color(0xFF2FBE85) else Color(0xFFF6455D),
            style = typography.subtitle1,
            modifier = Modifier.constrainAs(textChangeRate) {
                top.linkTo(textPrice.top)
                bottom.linkTo(textPrice.bottom)
                end.linkTo(parent.end)
            })
    }
}
