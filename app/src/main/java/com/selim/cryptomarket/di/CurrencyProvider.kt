package com.selim.cryptomarket.di

import com.selim.cryptomarket.ui.settings.CurrencyType
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyProvider @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    settingsDataStore: SettingsDataStore
) {

    val currencyCode: StateFlow<String> = settingsDataStore.currencyCode
        .onEach { currency ->
            Timber.d("Currency changed to: $currency")
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = CurrencyType.USD.value
        )

    fun getCurrentCurrency(): String {
        val currency = currencyCode.value
        Timber.d("getCurrentCurrency() returning: $currency")
        return currency
    }
}
