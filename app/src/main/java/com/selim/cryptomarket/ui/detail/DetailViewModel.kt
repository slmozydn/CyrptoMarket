package com.selim.cryptomarket.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selim.cryptomarket.data.CoinChartResponse
import com.selim.cryptomarket.data.CoinDetailResponse
import com.selim.cryptomarket.data.repository.CryptoRepository
import com.selim.cryptomarket.ui.detail.TimeRange.THIRTY_DAYS
import com.selim.cryptomarket.ui.settings.CurrencyType.USD
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val settingsDataStore: SettingsDataStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun getDetail(id: String) = viewModelScope.launch {
        _uiState.update { it.copy(isRefreshing = true, error = null) }

        runCatching {
            coroutineScope {
                val currencyCode = settingsDataStore.currencyCode.first()
                val currentTimeRange = _uiState.value.timeRange.value
                val detailDeferred = async { repository.getCoinDetail(id) }
                val chartDeferred = async {
                    runCatching { repository.getCoinChart(id, currentTimeRange) }.getOrNull()
                }

                Triple(detailDeferred.await(), chartDeferred.await(), currencyCode)
            }
        }.fold(
            onSuccess = { (detail, chart, currency) ->
                _uiState.update {
                    it.copy(
                        coinDetail = detail,
                        coinChart = chart,
                        currencyCode = currency,
                        isRefreshing = false,
                        loading = false,
                        error = null,
                    )
                }
            },
            onFailure = { exception ->
                _uiState.update {
                    it.copy(
                        error = exception,
                        isRefreshing = false,
                        loading = false,
                    )
                }
            },
        )
    }

    fun onTimeRangeChange(id: String, timeRange: TimeRange) = viewModelScope.launch {
        _uiState.update { it.copy(loading = true, error = null) }

        runCatching {
            repository.getCoinChart(id, timeRange.value)
        }.fold(
            onSuccess = { chart ->
                _uiState.update {
                    it.copy(
                        coinChart = chart,
                        timeRange = timeRange,
                        loading = false,
                        error = null,
                    )
                }
            },
            onFailure = { exception ->
                _uiState.update {
                    it.copy(
                        error = exception,
                        loading = false,
                    )
                }
            },
        )
    }

    data class DetailUiState(
        val coinDetail: CoinDetailResponse? = null,
        val currencyCode: String = USD.value,
        val coinChart: CoinChartResponse? = null,
        val timeRange: TimeRange = THIRTY_DAYS,
        val error: Throwable? = null,
        val loading: Boolean = true,
        val isRefreshing: Boolean = false,
    )
}
