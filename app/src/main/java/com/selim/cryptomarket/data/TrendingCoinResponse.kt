package com.selim.cryptomarket.data

import com.google.gson.annotations.SerializedName

data class TrendingCoinResponse(@SerializedName("item") val trendingCoin: Trending)

data class Trending(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("large") val imageUrl: String,
    @SerializedName("score") val score: Int,
    @SerializedName("price_btc") val currentPrice: Double,
    @SerializedName("market_cap_rank") val marketCapRank: Int
)
