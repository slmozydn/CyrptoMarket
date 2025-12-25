package com.selim.cryptomarket.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selim.cryptomarket.R.string
import com.selim.cryptomarket.data.SearchResult
import com.selim.cryptomarket.data.TrendingCoinResponse
import com.selim.cryptomarket.data.repository.CryptoRepository
import com.selim.cryptomarket.ui.search.SearchItem.Currency
import com.selim.cryptomarket.util.ErrorHandler
import com.selim.cryptomarket.ui.search.SearchItem.Nfts
import com.selim.cryptomarket.ui.search.SearchItem.SearchHistory
import com.selim.cryptomarket.ui.search.SearchItem.Title
import com.selim.cryptomarket.ui.search.SearchItem.Trending
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CryptoRepository,
    private val searchDataStore: SearchDataStore,
    private val errorHandler: ErrorHandler,
) : ViewModel() {

    private val queryChanges = MutableStateFlow("")

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        setQueryChanges()
    }

    @OptIn(FlowPreview::class)
    private fun setQueryChanges() {
        queryChanges
            .debounce(DEBOUNCE_MS)
            .distinctUntilChanged()
            .onEach { searchCoins(it) }
            .launchIn(viewModelScope)
    }

    private fun searchCoins(searchString: String) {
        // if (searchString.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            runCatching {
                coroutineScope {
                    val searchDeferred = async { repository.searchCoins(searchString) }

                    val trendingDeferred = async {
                        runCatching { repository.getTrendingCoins().coins }.getOrDefault(emptyList())
                    }

                    val historyDeferred = async {
                        runCatching { searchDataStore.searchQueries.first() }.getOrDefault("")
                    }

                    val searchResult = searchDeferred.await()
                    val trendingCoins = trendingDeferred.await()
                    val searchHistory = historyDeferred.await()

                    buildSearchItemsList(searchResult, trendingCoins, searchHistory)
                }
            }.fold(
                onSuccess = { items ->
                    _uiState.update {
                        it.copy(items = items, isLoading = false, errorMessage = null)
                    }
                },
                onFailure = { exception ->
                    val errorMessage = errorHandler.handleError(exception)
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = errorMessage)
                    }
                },
            )
        }
    }

    fun onSearch(query: String) {
        queryChanges.tryEmit(query)
    }

    suspend fun saveHistory(query: String) {
        if (query.isNotBlank()) {
            searchDataStore.updateSearchPreference(query)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            searchDataStore.clearSearchPreference()
            _uiState.update {
                it.copy(items = it.items.dropWhile { item -> item is SearchHistory })
            }
        }
    }

    private fun buildSearchItemsList(
        searchResult: SearchResult,
        trendingCoins: List<TrendingCoinResponse>,
        searchHistory: String,
    ): List<SearchItem> = buildList {
        val trending = trendingCoins
            .map(::Trending)
            .take(RESULT_COIN_SIZE)

        val coins = searchResult.coins
            .take(RESULT_COIN_SIZE)
            .map(::Currency)

        val nfts = searchResult.nfts
            .filter { it.thumb != EMPTY_IMAGE_URL }
            .take(RESULT_NFT_SIZE)

        if (searchHistory.isNotEmpty()) {
            val searchQueries = searchHistory.split(" ").reversed()
            add(SearchHistory(searchQueries))
        }

        if (coins.isNotEmpty()) {
            add(Title(string.coins))
            addAll(coins)
        }

        if (nfts.isNotEmpty()) {
            add(Title(string.nfts))
            add(Nfts(nfts))
        }

        if (trendingCoins.isNotEmpty()) {
            add(Title(string.trending))
            addAll(trending)
        }
    }

    data class SearchUiState(
        val items: List<SearchItem> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
    )

    companion object {
        private const val EMPTY_IMAGE_URL = "missing_thumb.png"
        private const val RESULT_COIN_SIZE = 5
        private const val RESULT_NFT_SIZE = 10
        private const val DEBOUNCE_MS = 400L
    }
}
