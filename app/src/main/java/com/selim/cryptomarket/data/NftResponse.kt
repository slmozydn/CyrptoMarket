package com.selim.cryptomarket.data

import kotlinx.serialization.Serializable

@Serializable
data class NftResponse(
    val id: String,
    val name: String,
    val symbol: String,
    val thumb: String
)
