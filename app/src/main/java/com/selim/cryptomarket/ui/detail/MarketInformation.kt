package com.selim.cryptomarket.ui.detail

import com.github.mikephil.charting.data.Entry

data class MarketInformation(
    val currentPrice: String,
    val athPrice: String,
    val atlPrice: String,
    val highPrice: String,
    val lowPrice: String,
    val averagePrice: String,
    val changePrice: String,
    val changeRate: String,
    val isPositive: Boolean,
    val aboutChart: String,
    val chartEntries: List<Entry>
)