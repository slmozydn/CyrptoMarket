package com.selim.cryptomarket.data

import kotlinx.serialization.Serializable

@Serializable
data class SearchTrendingResult(
    val coins: List<TrendingCoinResponse>,
)
