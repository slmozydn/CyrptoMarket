package com.selim.cryptomarket.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selim.cryptomarket.data.SearchData
import com.selim.cryptomarket.service.CryptoCurrencyService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val cryptoCurrencyService: CryptoCurrencyService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    @FlowPreview
    fun setQueryChanges(queryChanges: Flow<CharSequence?>) {
        queryChanges.drop(1)
            .debounce(DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { searchCoins(it.toString()) }
            .launchIn(viewModelScope)
    }

    private fun searchCoins(searchString: String) {
        _uiState.update { currentUiState -> currentUiState.copy(loading = true) }
        viewModelScope.launch {
            try {
                val response = cryptoCurrencyService.searchCoins(searchString)
                _uiState.update { currentUiState ->
                    val result = buildList {
                        addAll(response.coins.take(RESULT_SIZE))
                        addAll(response.nfts.filter { it.thumb != EMPTY_IMAGE_URL }.take(RESULT_SIZE))
                    }
                    currentUiState.copy(searchResult = result, loading = false)
                }
            } catch (exception: Exception) {
                _uiState.update { currentUiState ->
                    currentUiState.copy(error = exception, loading = false)
                }
            }
        }
    }

    data class SearchUiState(
        val searchResult: List<SearchData> = emptyList(),
        val loading: Boolean = false,
        val error: Throwable? = null
    )

    companion object {
        private const val EMPTY_IMAGE_URL = "missing_thumb.png"
        private const val RESULT_SIZE = 10
        private const val DEBOUNCE_MS = 400L
    }
}
