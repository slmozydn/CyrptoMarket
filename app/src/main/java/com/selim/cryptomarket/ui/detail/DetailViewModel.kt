package com.selim.cryptomarket.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selim.cryptomarket.data.CoinChartResponse
import com.selim.cryptomarket.data.CoinDetailResponse
import com.selim.cryptomarket.service.CryptoCurrencyService
import com.selim.cryptomarket.ui.settings.CurrencyType.USD
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val cryptoCurrencyService: CryptoCurrencyService,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    fun getDetail(id: String) = viewModelScope.launch {
        _uiState.value = try {
            coroutineScope {
                val currencyCode = runBlocking { settingsDataStore.currencyCode.first() }
                val result = async { cryptoCurrencyService.coinDetails(id) }
                val chart = async { cryptoCurrencyService.coinChart(id) }
                _uiState.value.copy(
                    coinDetail = result.await(),
                    currencyCode = currencyCode,
                    coinChart = chart.await(),
                    loading = false
                )
            }
        } catch (exception: Exception) {
            _uiState.value.copy(error = exception, loading = false)
        }
    }

    data class DetailUiState(
        val coinDetail: CoinDetailResponse? = null,
        val currencyCode: String = USD.value,
        val coinChart: CoinChartResponse? = null,
        val error: Throwable? = null,
        val loading: Boolean = true
    )
}
