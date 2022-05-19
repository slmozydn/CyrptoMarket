package com.selim.cryptomarket.data

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    val id: String,
    val name: String,
    val symbol: String,
    val large: String,
    @SerializedName("market_cap_rank") val marketCapRank: Int
)
