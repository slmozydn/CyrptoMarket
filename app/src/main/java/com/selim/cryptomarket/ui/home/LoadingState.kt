package com.selim.cryptomarket.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        CircularProgressIndicator(
            modifier = modifier.size(32.dp),
            color = MaterialTheme.colorScheme.tertiary,
        )

        Text(
            text = "Please wait...",
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.titleSmall,
            modifier = modifier.padding(top = 16.dp),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoadingPreview() {
    LoadingState()
}
