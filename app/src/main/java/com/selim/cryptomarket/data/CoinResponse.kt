package com.selim.cryptomarket.data

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    @SerializedName("id") var id: String,
    @SerializedName("symbol") var symbol: String,
    @SerializedName("name") var name: String,
    @SerializedName("image") var image: String,
    @SerializedName("current_price") var currentPrice: Double,
    @SerializedName("market_cap") var marketCap: Double? = null,
    @SerializedName("market_cap_rank") var marketCapRank: Int? = null,
    @SerializedName("total_volume") var totalVolume: Double? = null,
    @SerializedName("price_change_percentage_24h") var priceChangePercentage24h: Double? = null,
    @SerializedName("last_updated") var lastUpdated: String? = null,
    @Transient var currencyCode: String? = null
)
