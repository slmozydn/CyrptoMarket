package com.selim.cryptomarket.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(
    val id: String,
    val name: String,
    val symbol: String,
    val large: String,
    @SerialName("market_cap_rank") val marketCapRank: Int? = null,
)
