package com.selim.cryptomarket.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailResponse(
    @SerialName("description")
    val description: Description,
    @SerialName("id")
    val id: String,
    @SerialName("image")
    val image: Image,
    @SerialName("last_updated")
    val lastUpdated: String,
    @SerialName("market_cap_rank")
    val marketCapRank: Int,
    @SerialName("market_data")
    val marketData: MarketData,
    @SerialName("name")
    val name: String,
    @SerialName("symbol")
    val symbol: String,
)

@Serializable
data class Description(
    @SerialName("en")
    val en: String
)

@Serializable
data class Image(
    @SerialName("large")
    val large: String,
    @SerialName("small")
    val small: String
)

@Serializable
data class MarketData(
    @SerialName("ath")
    val ath: CurrencyType,
    @SerialName("atl")
    val atl: CurrencyType,
    @SerialName("current_price")
    val currentPrice: CurrencyType,
    @SerialName("high_24h")
    val high24h: CurrencyType,
    @SerialName("last_updated")
    val lastUpdated: String,
    @SerialName("low_24h")
    val low24h: CurrencyType,
    @SerialName("market_cap")
    val marketCap: CurrencyType,
    @SerialName("market_cap_rank")
    val marketCapRank: Int,
    @SerialName("price_change_24h_in_currency")
    val priceChange24hInCurrency: CurrencyType,
    @SerialName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,
    @SerialName("price_change_percentage_24h_in_currency")
    val priceChangePercentage24hInCurrency: CurrencyType,
    @SerialName("total_volume")
    val totalVolume: CurrencyType
)

@Serializable
data class CurrencyType(
    @SerialName("eur")
    val eur: Double,
    @SerialName("try")
    val tryX: Double,
    @SerialName("usd")
    val usd: Double
)
