package com.selim.cryptomarket.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selim.cryptomarket.R
import com.selim.cryptomarket.ui.search.SearchItem.Currency
import com.selim.cryptomarket.ui.search.SearchItem.Error
import com.selim.cryptomarket.ui.search.SearchItem.Loading
import com.selim.cryptomarket.ui.search.SearchItem.Nfts
import com.selim.cryptomarket.ui.search.SearchItem.Title
import com.selim.cryptomarket.service.CryptoCurrencyService
import com.selim.cryptomarket.ui.search.SearchItem.Trending
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
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

    private val queryChanges = MutableStateFlow("")
    private val _uiState = MutableStateFlow<List<SearchItem>>(emptyList())
    val uiState: StateFlow<List<SearchItem>> = _uiState

    init {
        setQueryChanges()
    }

    @OptIn(FlowPreview::class)
    private fun setQueryChanges() {
        queryChanges.drop(1)
            .debounce(DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { searchCoins(it) }
            .launchIn(viewModelScope)
    }

    internal fun onSearch(query: String) {
        queryChanges.tryEmit(query)
    }

    private fun searchCoins(searchString: String) {
        viewModelScope.launch {
            _uiState.emit(listOf(Loading))
            try {
                val searchResponse = async { cryptoCurrencyService.search(searchString) }
                val trendingResponse = async { cryptoCurrencyService.searchTrending() }
                val result = buildList {
                    val searchResult = searchResponse.await()
                    val trendingCoins = trendingResponse.await().coins.map(::Trending).take(RESULT_COIN_SIZE)
                    val coins = searchResult.coins.take(RESULT_COIN_SIZE).map(::Currency)
                    val nfts = searchResult.nfts.filter { it.thumb != EMPTY_IMAGE_URL }.take(RESULT_NFT_SIZE)

                    if (coins.isNotEmpty()) {
                        add(Title(R.string.coins))
                        addAll(coins)
                    }
                    if (nfts.isNotEmpty()) {
                        add(Title(R.string.nfts))
                        add(Nfts(nfts))
                    }
                    if (trendingCoins.isNotEmpty()) {
                        add(Title(R.string.trending))
                        addAll(trendingCoins)
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
        private const val RESULT_COIN_SIZE = 5
        private const val RESULT_NFT_SIZE = 10
        private const val DEBOUNCE_MS = 400L
    }
}
