package com.selim.cryptomarket.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.selim.cryptomarket.R

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    message: String = "",
    backgroundColor: Color = MaterialTheme.colors.background
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_error),
            contentDescription = null,
            tint = MaterialTheme.colors.primaryVariant
        )
        Text(
            text = "Something went wrong !",
            color = MaterialTheme.colors.primaryVariant,
            style = MaterialTheme.typography.subtitle2,
            modifier = modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ErrorColumnPreview() {
    ErrorState()
}
