package com.selim.cryptomarket.ui.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.selim.cryptomarket.ui.settings.ChangeCurrencyBottomSheetFragment.CurrencyType.USD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyDataStore @Inject constructor(private val settingsDataStore: DataStore<Preferences>) {

    val currencyCode: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[KEY_CURRENCY] ?: USD.value
    }

    suspend fun selectCurrencyPreference(currencyCode: String) {
        settingsDataStore.edit { settings ->
            settings[KEY_CURRENCY] = currencyCode
        }
    }

    companion object {
        val KEY_CURRENCY = stringPreferencesKey("currencyCode")
    }
}