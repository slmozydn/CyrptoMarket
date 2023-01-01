package com.selim.cryptomarket.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selim.cryptomarket.service.CryptoCurrencyService
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val cryptoCurrencyService: CryptoCurrencyService,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState(null, "USD", null))
    val uiState: StateFlow<DetailUiState> = _uiState

    fun getDetail(id: String) = viewModelScope.launch {
        val result = async { cryptoCurrencyService.coinDetails(id) }.await()
        val chart = async { cryptoCurrencyService.coinChart(id) }.await()
        _uiState.value = DetailUiState(result, settingsDataStore.currencyCode.first(), chart)
    }

    data class DetailUiState(
        val coinDetail: CoinDetailResponse?,
        val currencyCode: String,
        val coinChart: CoinChartResponse?
    )
}
