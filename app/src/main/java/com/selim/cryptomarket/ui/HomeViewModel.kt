package com.selim.cryptomarket.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.service.CryptoCurrencyService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cryptoCurrencyService: CryptoCurrencyService
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(loading = true))
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchCoins()
    }

    private fun fetchCoins() = viewModelScope.launch {
        try {
            val result = cryptoCurrencyService.fetchCoins()
            _uiState.update { it.copy(loading = false, coins = result) }
        } catch (exception: Exception) {
            _uiState.update { it.copy(loading = false, error = exception) }
        }
    }

    data class UiState(
        val coins: List<CoinResponse> = emptyList(),
        val loading: Boolean = false,
        val error: Throwable? = null
    )
}
