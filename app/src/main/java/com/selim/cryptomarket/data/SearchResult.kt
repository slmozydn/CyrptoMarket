package com.selim.cryptomarket.data

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val coins: List<CurrencyResponse>,
    val nfts: List<NftResponse>,
)
