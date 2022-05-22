package com.selim.cryptomarket.util

import com.selim.cryptomarket.ui.settings.ChangeCurrencyBottomSheetFragment.CurrencyType.EUR
import com.selim.cryptomarket.ui.settings.ChangeCurrencyBottomSheetFragment.CurrencyType.TRY
import com.selim.cryptomarket.ui.settings.ChangeCurrencyBottomSheetFragment.CurrencyType.USD
import java.text.DecimalFormat

fun Double?.formatPercentage(): String {
    if (this == null || this == 0.0) return "0.0%"

    val percentage = DecimalFormat("#.##").format(this)
    return if (this > 0) {
        "+$percentage%"
    } else {
        "$percentage%"
    }
}

fun Double?.formatPrice(currencyCode: String? = null): String {
    // TODO val price = DecimalFormat("###.00").format(this)
    return when (currencyCode) {
        USD.value -> "$this $"
        TRY.value -> "$this ₺"
        EUR.value -> "$this €"
        else -> this.toString()
    }
}

fun Double?.formatVolume(): String = "Hacim ${this.formatPrice()}"

fun Int.formatMarketCap(): String = "Market Cap ${this.toDouble().formatPrice()}"
