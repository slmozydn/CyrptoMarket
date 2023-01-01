package com.selim.cryptomarket.util

import com.selim.cryptomarket.ui.settings.CurrencyType.TRY
import com.selim.cryptomarket.ui.settings.CurrencyType.USD
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.ln
import kotlin.math.pow

fun Double?.formatPercentage(): String {
    if (this == null || this == 0.0) return "0.00%"

    val percentage = DecimalFormat("#.##").format(this)
    return if (this > 0) {
        "+$percentage%"
    } else {
        "$percentage%"
    }
}

fun Double?.formatPrice(currencyCode: String? = null): String {
    val locale = when (currencyCode) {
        USD.value -> Locale("en", "US")
        TRY.value -> Locale("tr", "TR")
        else -> Locale("en", "DE")
    }
    return NumberFormat
        .getCurrencyInstance(locale)
        .format(this ?: 0.0)
        .orEmpty()
}

fun Double?.formatVolume(): String {
    if (this == null) return "Volume 0.0"

    if (this.toLong() < 1000) return "$this"
    val exp = (ln(this) / ln(1000.0)).toInt()
    return "Volume %.1f %c".format(this / 1000.0.pow(exp.toDouble()), "kMBTPE"[exp - 1])
}

fun Int.formatMarketCap(): String = "Market Cap: $this"

fun Int.formatScore(): String = "#${this + 1}"
