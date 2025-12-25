package com.selim.cryptomarket.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.selim.cryptomarket.R

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Icon(
            modifier = modifier
                .size(80.dp)
                .padding(bottom = 16.dp),
            painter = painterResource(id = R.drawable.icon_error),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
        )

        Text(
            text = "Something went wrong !",
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ErrorColumnPreview() {
    ErrorState()
}
