package com.selim.cryptomarket.data

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    val id: String,
    val name: String,
    val symbol: String,
    @SerializedName("market_cap_rank") val marketCapRank: Int,
    val thumb: String,
    val large: String
)
