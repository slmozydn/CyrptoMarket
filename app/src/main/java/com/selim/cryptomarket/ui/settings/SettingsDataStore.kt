package com.selim.cryptomarket.ui.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.selim.cryptomarket.ui.settings.ChangeCurrencyBottomSheetFragment.CurrencyType.USD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsDataStore @Inject constructor(private val settingsDataStore: DataStore<Preferences>) {

    val currencyCode: Flow<String> = settingsDataStore.data.map { preferences ->
        preferences[KEY_CURRENCY] ?: USD.value
    }

    val isDarkMode: Flow<Boolean> = settingsDataStore.data.map { preferences ->
        preferences[KEY_DARK_THEME] ?: false
    }

    suspend fun changeCurrencyPreference(currencyCode: String) {
        settingsDataStore.edit { settings ->
            settings[KEY_CURRENCY] = currencyCode
        }
    }

    suspend fun changeThemePreference() {
        settingsDataStore.edit { settings ->
            settings[KEY_DARK_THEME] = isDarkMode.first().not()
        }
    }

    companion object {
        val KEY_CURRENCY = stringPreferencesKey("currencyCode")
        val KEY_DARK_THEME = booleanPreferencesKey("darkTheme")
    }
}