package com.selim.cryptomarket.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.service.CryptoCurrencyService
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cryptoCurrencyService: CryptoCurrencyService,
    private val dataStore: SettingsDataStore
) : ViewModel() {

    val onChangeTheme = dataStore.isDarkMode.asLiveData()

    // Currency code'u Flow olarak tut ve PagingSource'a geçir
    // İlk değer için default kullan, sonra güncelle
    val coins: Flow<PagingData<CoinResponse>> = dataStore.currencyCode
        .flatMapLatest { currencyCode ->
            Pager(PagingConfig(pageSize = PAGE_SIZE)) {
                CoinsPagingSource(cryptoCurrencyService, currencyCode)
            }.flow
        }
        .cachedIn(viewModelScope)

    fun onChangeTheme() = viewModelScope.launch {
        dataStore.changeThemePreference()
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
