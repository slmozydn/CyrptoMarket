package com.selim.cryptomarket.data

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("current_price") val currentPrice: Double,
    @SerializedName("market_cap") val marketCap: Double? = null,
    @SerializedName("market_cap_rank") val marketCapRank: Int? = null,
    @SerializedName("total_volume") val totalVolume: Double? = null,
    @SerializedName("price_change_percentage_24h") val priceChangePercentage24h: Double? = null,
    @SerializedName("last_updated") val lastUpdated: String? = null,
    @Transient val currencyCode: String? = null
)
