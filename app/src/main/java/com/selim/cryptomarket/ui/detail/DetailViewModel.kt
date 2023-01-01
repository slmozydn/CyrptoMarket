package com.selim.cryptomarket.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selim.cryptomarket.data.CoinChartResponse
import com.selim.cryptomarket.data.CoinDetailResponse
import com.selim.cryptomarket.service.CryptoCurrencyService
import com.selim.cryptomarket.ui.detail.TimeRange.THIRTY_DAYS
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
    savedStateHandle: SavedStateHandle,
    private val cryptoCurrencyService: CryptoCurrencyService,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState(loading = true))
    val uiState: StateFlow<DetailUiState> = _uiState

    init {
        val id = savedStateHandle.get<String>("id")!!
        getDetail(id)
    }

    fun getDetail(id: String) = viewModelScope.launch {
        _uiState.value = try {
            coroutineScope {
                val currencyCode = runBlocking { settingsDataStore.currencyCode.first() }
                val result = async { cryptoCurrencyService.coinDetails(id) }
                val chart = async { cryptoCurrencyService.coinChart(id, THIRTY_DAYS.value) }
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

    fun onTimeRangeChange(id: String, timeRange: TimeRange) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(loading = true)
        _uiState.value = try {
            val chart = cryptoCurrencyService.coinChart(id, timeRange.value)
            _uiState.value.copy(coinChart = chart, timeRange = timeRange, loading = false)
        } catch (exception: Exception) {
            _uiState.value.copy(error = exception, loading = false)
        }
    }

    data class DetailUiState(
        val coinDetail: CoinDetailResponse? = null,
        val currencyCode: String = USD.value,
        val coinChart: CoinChartResponse? = null,
        val timeRange: TimeRange = THIRTY_DAYS,
        val error: Throwable? = null,
        val loading: Boolean = true
    )
}
