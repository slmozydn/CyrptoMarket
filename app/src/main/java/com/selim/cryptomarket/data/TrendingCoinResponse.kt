package com.selim.cryptomarket.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingCoinResponse(@SerialName("item") val trendingCoin: Trending)

@Serializable
data class Trending(
    @SerialName("id") val id: String,
    @SerialName("symbol") val symbol: String,
    @SerialName("name") val name: String,
    @SerialName("large") val imageUrl: String,
    @SerialName("score") val score: Int,
    @SerialName("price_btc") val currentPrice: Double,
    @SerialName("market_cap_rank") val marketCapRank: Int
)
