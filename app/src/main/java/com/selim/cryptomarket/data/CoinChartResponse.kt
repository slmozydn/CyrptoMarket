package com.selim.cryptomarket.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinChartResponse(
    @SerialName("prices")
    val prices: List<List<Double>>
)
