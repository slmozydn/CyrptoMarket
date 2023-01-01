package com.selim.cryptomarket.ui.detail

import com.google.gson.annotations.SerializedName

data class CoinDetailResponse(
    @SerializedName("coingecko_rank")
    val coingeckoRank: Int,
    @SerializedName("description")
    val description: Description,
    @SerializedName("genesis_date")
    val genesisDate: String,
    @SerializedName("hashing_algorithm")
    val hashingAlgorithm: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: Image,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("market_data")
    val marketData: MarketData,
    @SerializedName("name")
    val name: String,
    @SerializedName("public_notice")
    val publicNotice: Any,
    @SerializedName("symbol")
    val symbol: String,
)

data class Description(
    @SerializedName("en")
    val en: String
)

data class Image(
    @SerializedName("large")
    val large: String,
    @SerializedName("small")
    val small: String,
    @SerializedName("thumb")
    val thumb: String
)

data class MarketData(
    @SerializedName("ath")
    val ath: CurrencyType,
    @SerializedName("atl")
    val atl: CurrencyType,
    @SerializedName("current_price")
    val currentPrice: CurrencyType,
    @SerializedName("high_24h")
    val high24h: CurrencyType,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("low_24h")
    val low24h: CurrencyType,
    @SerializedName("market_cap")
    val marketCap: CurrencyType,
    @SerializedName("market_cap_change_24h")
    val marketCapChange24h: Double,
    @SerializedName("market_cap_change_percentage_24h")
    val marketCapChangePercentage24h: Double,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("max_supply")
    val maxSupply: Double,
    @SerializedName("price_change_24h")
    val priceChange24h: Double,
    @SerializedName("price_change_24h_in_currency")
    val priceChange24hInCurrency: CurrencyType,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,
    @SerializedName("price_change_percentage_24h_in_currency")
    val priceChangePercentage24hInCurrency: CurrencyType,
    @SerializedName("total_supply")
    val totalSupply: Double,
    @SerializedName("total_volume")
    val totalVolume: CurrencyType
)

data class CurrencyType(
    @SerializedName("eur")
    val eur: Double,
    @SerializedName("try")
    val tryX: Double,
    @SerializedName("usd")
    val usd: Double
)
