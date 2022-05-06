package com.selim.cryptomarket.data

import com.google.gson.annotations.SerializedName

data class CoinResult(
    val id: String,
    val name: String,
    val symbol: String,
    val thumb: String,
    val large: String,
    @SerializedName("market_cap_rank") val marketCapRank: Int,
)
