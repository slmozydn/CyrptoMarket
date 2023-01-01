package com.selim.cryptomarket.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: SettingsDataStore
) : ViewModel() {

    val currencyCode = dataStore.currencyCode

    fun onCurrencySelected(currencyCode: String) {
        viewModelScope.launch() {
            if (currencyCode == dataStore.currencyCode.first()) return@launch

            dataStore.changeCurrencyPreference(currencyCode)
        }
    }
}