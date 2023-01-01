package com.selim.cryptomarket.ui.detail

import com.google.gson.annotations.SerializedName

data class CoinChartResponse(
    @SerializedName("prices")
    val prices: List<List<Double>>
)
