package com.selim.cryptomarket.ui.home

data class CoinUiModel(
    val id: String,
    val name: String,
    val symbol: String,
    val imageUrl: String,
    val price: String,
    val volume: String,
    val changePercentage: String,
    val isChangePositive: Boolean,
    val changeColorType: ChangeColorType,
)

enum class ChangeColorType {
    POSITIVE,
    NEGATIVE,
    NEUTRAL,
}
