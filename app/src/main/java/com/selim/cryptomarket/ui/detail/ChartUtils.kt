package com.selim.cryptomarket.ui.detail

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.selim.cryptomarket.R

object ChartUtils {
    fun createLineDataSet(
        context: Context,
        isPositive: Boolean,
        chartValues: List<Entry>,
    ): LineDataSet {
        return LineDataSet(chartValues, "market_price").apply {
            val (colorRes, highLightColorRes) = if (isPositive) {
                R.color.greenish_teal to R.color.black
            } else {
                R.color.watermelon to R.color.black
            }

            mode = LineDataSet.Mode.CUBIC_BEZIER
            color = ContextCompat.getColor(context, colorRes)
            highLightColor = ContextCompat.getColor(context, highLightColorRes)

            val drawableRes = if (isPositive) {
                R.drawable.background_positive_chart
            } else {
                R.drawable.background_negative_chart
            }
            fillDrawable = ContextCompat.getDrawable(context, drawableRes)

            lineWidth = 1f
            setDrawFilled(true)
            setDrawCircles(false)
        }
    }
}
