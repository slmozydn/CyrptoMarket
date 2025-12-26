package com.selim.cryptomarket.ui.detail

import com.github.mikephil.charting.data.Entry

data class CoinDisplayModel(
    val name: String,
    val symbol: String,
    val iconUrl: String,
    val currentPrice: String,
    val highestPrice: String,
    val lowestPrice: String,
    val changePrice: String,
    val changePercentage: String,
    val athPrice: String,
    val atlPrice: String,
    val volume: String,
    val isChangePositive: Boolean,
    val description: String,
    val chartEntries: List<Entry>?,
)
