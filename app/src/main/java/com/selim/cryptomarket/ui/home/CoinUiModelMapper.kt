package com.selim.cryptomarket.ui.home

import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.util.formatPercentage
import com.selim.cryptomarket.util.formatPrice
import com.selim.cryptomarket.util.formatSymbol
import com.selim.cryptomarket.util.formatVolume

object CoinUiModelMapper {
    fun CoinResponse.mapToUiModel(): CoinUiModel {
        val percentage = priceChangePercentage24h ?: 0.0
        val colorType = when {
            percentage > 0 -> ChangeColorType.POSITIVE
            percentage < 0 -> ChangeColorType.NEGATIVE
            else -> ChangeColorType.NEUTRAL
        }

        return CoinUiModel(
            id = id,
            name = name.take(10),
            symbol = symbol.formatSymbol(),
            imageUrl = image,
            price = currentPrice.formatPrice(currencyCode),
            volume = totalVolume.formatVolume(),
            changePercentage = priceChangePercentage24h.formatPercentage(),
            isChangePositive = percentage > 0,
            changeColorType = colorType,
        )
    }
}
