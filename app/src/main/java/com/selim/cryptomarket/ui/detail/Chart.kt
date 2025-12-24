package com.selim.cryptomarket.ui.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

private const val DEFAULT_ANIMATE_XY_DURATION: Int = 300
private const val MIN_ENTRY_COUNT_FOR_ANIMATION: Int = 30

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
                axisLeft.textColor = 0xFF888D91.toInt()
                axisRight.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(false)
                setScaleEnabled(false)
                setDrawGridBackground(false)
                setDrawBorders(false)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(300.dp),
        update = { chart ->
            updateChartData(chart, lineDataSet)
        }
    )
}

private fun updateChartData(chart: LineChart, lineDataSet: LineDataSet?) {
    if (lineDataSet != null) {
        chart.clear()
        chart.data = LineData(lineDataSet).apply {
            setDrawValues(false)
        }
        
        if (lineDataSet.entryCount > MIN_ENTRY_COUNT_FOR_ANIMATION) {
            chart.animateX(DEFAULT_ANIMATE_XY_DURATION)
        }
        
        chart.invalidate()
    }
}
