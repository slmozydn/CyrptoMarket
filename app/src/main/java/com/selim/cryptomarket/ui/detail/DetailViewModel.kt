package com.selim.cryptomarket.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selim.cryptomarket.data.CoinChartResponse
import com.selim.cryptomarket.data.CoinDetailResponse
import com.selim.cryptomarket.data.repository.CryptoRepository
import com.selim.cryptomarket.ui.detail.TimeRange.THIRTY_DAYS
import com.selim.cryptomarket.ui.settings.CurrencyType.USD
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import com.selim.cryptomarket.util.ErrorHandler
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
    private val errorHandler: ErrorHandler,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var coinDetailResponse: CoinDetailResponse? = null
    private var currencyCode: String = USD.value
    private var coinChart: CoinChartResponse? = null

    fun getDetail(id: String) = viewModelScope.launch {
        _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }

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
            onSuccess = { (detail, chart, code) ->
                coinDetailResponse = detail
                currencyCode = code
                coinChart = chart
                val displayModel = mapToDisplayModel(detail, code, chart)
                _uiState.update {
                    it.copy(
                        displayModel = displayModel,
                        isRefreshing = false,
                        loading = false,
                        errorMessage = null,
                    )
                }
            },
            onFailure = { exception ->
                _uiState.update {
                    it.copy(
                        errorMessage = errorHandler.handleError(exception),
                        isRefreshing = false,
                        loading = false,
                    )
                }
            },
        )
    }

    fun onTimeRangeChange(id: String, timeRange: TimeRange) = viewModelScope.launch {
        _uiState.update { it.copy(loading = true, errorMessage = null) }

        runCatching {
            repository.getCoinChart(id, timeRange.value)
        }.fold(
            onSuccess = { chart ->
                coinChart = chart
                val displayModel = coinDetailResponse?.let { response ->
                    mapToDisplayModel(response, currencyCode, chart)
                }
                _uiState.update {
                    it.copy(
                        timeRange = timeRange,
                        displayModel = displayModel,
                        loading = false,
                        errorMessage = null,
                    )
                }
            },
            onFailure = { exception ->
                _uiState.update {
                    it.copy(
                        errorMessage = errorHandler.handleError(exception),
                        loading = false,
                    )
                }
            },
        )
    }

    private fun mapToDisplayModel(
        coinResponse: CoinDetailResponse,
        currencyCode: String,
        coinChart: CoinChartResponse?,
    ): CoinDisplayModel {
        return CoinDisplayModelMapper.mapToDisplayModel(
            coinResponse = coinResponse,
            currencyCode = currencyCode,
            coinChart = coinChart,
        )
    }

    data class DetailUiState(
        val displayModel: CoinDisplayModel? = null,
        val timeRange: TimeRange = THIRTY_DAYS,
        val errorMessage: String? = null,
        val loading: Boolean = true,
        val isRefreshing: Boolean = false,
    )
}
