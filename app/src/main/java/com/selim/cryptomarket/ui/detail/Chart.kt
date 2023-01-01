package com.selim.cryptomarket.ui.detail

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun Chart(
    modifier: Modifier = Modifier,
    lineDataSet: LineDataSet? = null,
) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                isDragEnabled = false
                xAxis.isEnabled = false
                axisLeft.setDrawAxisLine(false)
                axisLeft.textColor = Color.BLACK
                axisRight.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(false)
                setScaleEnabled(false)
                setDrawGridBackground(false)
                setDrawBorders(false)
                invalidate()

                setLineDataSet(lineDataSet = lineDataSet)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(300.dp)
    )
}

private const val DEFAULT_ANIMATE_XY_DURATION: Int = 300
private const val MIN_ENTRY_COUNT_FOR_ANIMATION: Int = 30

@BindingAdapter("setLineDataSet", "animateXDuration", requireAll = false)
fun LineChart.setLineDataSet(
    lineDataSet: LineDataSet? = null,
    animateXDuration: Int = 0
) {
    if (lineDataSet != null) {
        clear()
        data = LineData(lineDataSet).apply {
            setDrawValues(false)
        }
    }

    if ((lineDataSet?.entryCount ?: 0) > MIN_ENTRY_COUNT_FOR_ANIMATION) {
        animateX(if (animateXDuration > 0) animateXDuration else DEFAULT_ANIMATE_XY_DURATION)
    }
}
