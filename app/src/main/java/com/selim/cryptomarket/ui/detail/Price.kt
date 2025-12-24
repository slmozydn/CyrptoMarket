package com.selim.cryptomarket.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Price(
    modifier: Modifier = Modifier,
    athPrice: String,
    atlPrice: String,
    highPrice: String,
    lowPrice: String,
    averagePrice: String,
    changePrice: String
) {
    Column(modifier = modifier) {
        Text(
            text = "Price",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSecondary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                PriceRow(label = "Ath", value = athPrice)
                PriceRow(label = "High", value = highPrice)
                PriceRow(label = "Volume", value = averagePrice)
            }

            VerticalDivider(
                modifier = Modifier
                    .height(84.dp)
                    .padding(horizontal = 24.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.onSecondary
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                PriceRow(label = "Atl", value = atlPrice)
                PriceRow(label = "Low", value = lowPrice)
                PriceRow(label = "Change", value = changePrice)
            }
        }
    }
}

@Composable
private fun PriceRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PricePreview() {
    Price(
        athPrice = "$50,000.00",
        atlPrice = "$50,000.00",
        highPrice = "$50,000.00",
        lowPrice = "$50,000.00",
        averagePrice = "$50,000.00",
        changePrice = "$50,000.00",
        modifier = Modifier.fillMaxWidth()
    )
}