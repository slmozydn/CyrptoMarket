package com.selim.cryptomarket.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val dataStore: SettingsDataStore
) : ViewModel() {
    var isDarkThemeEnabled = dataStore.isDarkMode
        private set

    fun onChangeTheme() = viewModelScope.launch {
        dataStore.changeThemePreference()
    }
}
