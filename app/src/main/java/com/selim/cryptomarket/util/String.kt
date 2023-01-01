package com.selim.cryptomarket.util

import java.util.Locale

fun String?.formatSymbol(): String = "/${this?.uppercase(Locale.getDefault())}"
