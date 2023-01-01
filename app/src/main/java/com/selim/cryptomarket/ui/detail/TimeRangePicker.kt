package com.selim.cryptomarket.ui.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class TimeRange {
    ONE_DAY, SEVEN_DAYS, THIRTY_DAYS, SIXTY_DAYS, NINETY_DAYS, ONE_YEAR;
}

@Composable
fun TimeRangePicker(
    modifier: Modifier = Modifier,
    selectedTimeRange: TimeRange = TimeRange.THIRTY_DAYS,
    onTimeRangeSelected: (TimeRange) -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TimeRangeChip(
            time = "24h",
            isSelected = selectedTimeRange == TimeRange.ONE_DAY
        ) {
            onTimeRangeSelected(TimeRange.ONE_DAY)
        }

        TimeRangeChip(
            time = "7d",
            isSelected = selectedTimeRange == TimeRange.SEVEN_DAYS
        ) {
            onTimeRangeSelected(TimeRange.SEVEN_DAYS)
        }

        TimeRangeChip(
            time = "14d",
            isSelected = selectedTimeRange == TimeRange.THIRTY_DAYS
        ) {
            onTimeRangeSelected(TimeRange.THIRTY_DAYS)
        }

        TimeRangeChip(
            time = "30d",
            isSelected = selectedTimeRange == TimeRange.SIXTY_DAYS
        ) {
            onTimeRangeSelected(TimeRange.SIXTY_DAYS)
        }

        TimeRangeChip(
            time = "60d",
            isSelected = selectedTimeRange == TimeRange.NINETY_DAYS
        ) {
            onTimeRangeSelected(TimeRange.NINETY_DAYS)
        }

        TimeRangeChip(
            time = "1y",
            isSelected = selectedTimeRange == TimeRange.ONE_YEAR
        ) {
            onTimeRangeSelected(TimeRange.ONE_YEAR)
        }
    }
}

@Composable
private fun TimeRangeChip(
    time: String,
    isSelected: Boolean,
    onTimeRangeSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) colors.secondaryVariant else colors.secondaryVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onTimeRangeSelected() }
            .requiredWidth(48.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp),
            text = time,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle2,
            color = if (isSelected) colors.primaryVariant else colors.primaryVariant
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimeRangePickerPreview() {
    TimeRangePicker(modifier = Modifier.fillMaxWidth())
}