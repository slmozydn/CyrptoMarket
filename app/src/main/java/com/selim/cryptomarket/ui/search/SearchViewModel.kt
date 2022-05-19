package com.selim.cryptomarket.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selim.cryptomarket.ui.search.SearchItem.Currency
import com.selim.cryptomarket.ui.search.SearchItem.Error
import com.selim.cryptomarket.ui.search.SearchItem.Loading
import com.selim.cryptomarket.ui.search.SearchItem.Nft
import com.selim.cryptomarket.ui.search.SearchItem.Title
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val cryptoCurrencyService: CryptoCurrencyService
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<SearchItem>>(emptyList())
    val uiState: StateFlow<List<SearchItem>> = _uiState

    @FlowPreview
    fun setQueryChanges(queryChanges: Flow<CharSequence?>) {
        queryChanges.drop(1)
            .debounce(DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { searchCoins(it.toString()) }
            .launchIn(viewModelScope)
    }

    private fun searchCoins(searchString: String) {
        viewModelScope.launch {
            _uiState.emit(listOf(Loading))
            try {
                val response = cryptoCurrencyService.searchCoins(searchString)
                val result = buildList {
                    val coins = response.coins.take(RESULT_SIZE).map(::Currency)
                    val nfts = response.nfts.filter { it.thumb != EMPTY_IMAGE_URL }.take(RESULT_SIZE).map(::Nft)

                    if (coins.isNotEmpty()) {
                        add(Title("Coins"))
                        addAll(coins)
                    }
                    if (nfts.isNotEmpty()) {
                        add(Title("Nfts"))
                        addAll(nfts)
                    }
                }
                _uiState.emit(result)
            } catch (exception: Exception) {
                _uiState.emit(listOf(Error))
            }
        }
    }

    companion object {
        private const val EMPTY_IMAGE_URL = "missing_thumb.png"
        private const val RESULT_SIZE = 10
        private const val DEBOUNCE_MS = 400L
    }
}
