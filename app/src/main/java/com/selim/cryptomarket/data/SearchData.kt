package com.selim.cryptomarket.data

import com.google.gson.annotations.SerializedName

sealed class SearchData {
    data class CurrencyResponse(
        val id: String,
        val name: String,
        val symbol: String,
        val thumb: String,
        val large: String,
        @SerializedName("market_cap_rank") val marketCapRank: Int
    ) : SearchData()

    data class NftResponse(
        val id: String,
        val name: String,
        val symbol: String,
        val thumb: String
    ) : SearchData()
}
