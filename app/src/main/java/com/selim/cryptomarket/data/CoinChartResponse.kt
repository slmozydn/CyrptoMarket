package com.selim.cryptomarket.data

import com.google.gson.annotations.SerializedName

data class CoinChartResponse(
    @SerializedName("prices")
    val prices: List<List<Double>>
)
