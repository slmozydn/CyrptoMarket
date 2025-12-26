package com.selim.cryptomarket.ui.detail

import com.github.mikephil.charting.data.Entry
import com.selim.cryptomarket.data.CoinChartResponse
import com.selim.cryptomarket.data.CoinDetailResponse
import com.selim.cryptomarket.data.CurrencyType
import com.selim.cryptomarket.ui.settings.CurrencyType.TRY
import com.selim.cryptomarket.ui.settings.CurrencyType.USD
import com.selim.cryptomarket.util.formatPercentage
import com.selim.cryptomarket.util.formatPrice
import com.selim.cryptomarket.util.formatVolume

object CoinDisplayModelMapper {
    fun mapToDisplayModel(
        coinResponse: CoinDetailResponse,
        currencyCode: String,
        coinChart: CoinChartResponse?,
    ): CoinDisplayModel {

        fun CurrencyType.getVal(code: String): Double = when (code) {
            USD.value -> usd
            TRY.value -> tryX
            else -> eur
        }

        val marketData = coinResponse.marketData
        val isPositive = marketData.priceChangePercentage24h > 0

        return CoinDisplayModel(
            name = coinResponse.name,
            symbol = coinResponse.symbol.uppercase(),
            iconUrl = coinResponse.image.small,
            currentPrice = marketData.currentPrice.getVal(currencyCode).formatPrice(currencyCode),
            highestPrice = marketData.high24h.getVal(currencyCode).formatPrice(currencyCode),
            lowestPrice = marketData.low24h.getVal(currencyCode).formatPrice(currencyCode),
            changePrice = marketData.priceChange24hInCurrency.getVal(currencyCode).formatPrice(currencyCode),
            changePercentage = marketData.priceChangePercentage24h.formatPercentage(),
            athPrice = marketData.ath.getVal(currencyCode).formatPrice(),
            atlPrice = marketData.atl.getVal(currencyCode).formatPrice(),
            volume = marketData.totalVolume.getVal(currencyCode).formatVolume().replace("Volume ", ""),
            isChangePositive = isPositive,
            description = coinResponse.description.en,
            chartEntries = coinChart?.prices?.map {
                Entry(it[0].toFloat(), it[1].toFloat())
            },
        )
    }
}
