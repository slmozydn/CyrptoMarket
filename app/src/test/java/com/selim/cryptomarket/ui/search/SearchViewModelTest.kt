package com.selim.cryptomarket.ui.search

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.selim.cryptomarket.data.SearchResult
import com.selim.cryptomarket.data.SearchTrendingResult
import com.selim.cryptomarket.data.Trending
import com.selim.cryptomarket.data.TrendingCoinResponse
import com.selim.cryptomarket.data.repository.CryptoRepository
import com.selim.cryptomarket.util.ErrorHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var repository: CryptoRepository
    private lateinit var searchDataStore: SearchDataStore
    private lateinit var errorHandler: ErrorHandler
    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val mockSearchResult = SearchResult(
        coins = emptyList(),
        nfts = emptyList(),
    )

    private val mockTrendingCoins = listOf(
        TrendingCoinResponse(
            trendingCoin = Trending(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                imageUrl = "",
                score = 1,
                currentPrice = 45000.0,
                marketCapRank = 1,
            ),
        ),
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        searchDataStore = mockk()
        errorHandler = mockk()

        coEvery { searchDataStore.searchQueries } returns flowOf("")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        coEvery { repository.searchCoins(any()) } returns mockSearchResult
        coEvery { repository.getTrendingCoins() } returns SearchTrendingResult(mockTrendingCoins)

        viewModel = SearchViewModel(repository, searchDataStore, errorHandler)

        val state = viewModel.uiState.value
        assertThat(state.searchQuery).isEmpty()
        assertThat(state.items).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `onSearch updates search query`() = runTest {
        coEvery { repository.searchCoins(any()) } returns mockSearchResult
        coEvery { repository.getTrendingCoins() } returns SearchTrendingResult(mockTrendingCoins)
        every { errorHandler.handleError(any()) } returns "Error"

        viewModel = SearchViewModel(repository, searchDataStore, errorHandler)
        val query = "bitcoin"

        viewModel.onSearch(query)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchQuery).isEqualTo(query)
        }
    }

    @Test
    fun `search is debounced for 400ms`() = runTest {
        viewModel = SearchViewModel(repository, searchDataStore, errorHandler)
        coEvery { repository.searchCoins(any()) } returns mockSearchResult
        coEvery { repository.getTrendingCoins() } returns SearchTrendingResult(mockTrendingCoins)

        viewModel.onSearch("b")
        advanceTimeBy(100)
        viewModel.onSearch("bi")
        advanceTimeBy(100)
        viewModel.onSearch("bit")
        advanceTimeBy(100)
        viewModel.onSearch("bitcoin")

        advanceTimeBy(400)
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.searchCoins("bitcoin") }
    }

    @Test
    fun `searchCoins updates state with results`() = runTest {
        val query = "bitcoin"
        viewModel = SearchViewModel(repository, searchDataStore, errorHandler)
        coEvery { repository.searchCoins(query) } returns mockSearchResult
        coEvery { repository.getTrendingCoins() } returns SearchTrendingResult(mockTrendingCoins)

        viewModel.onSearch(query)
        advanceTimeBy(500) // Wait for debounce
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.errorMessage).isNull()
            assertThat(state.items).isNotEmpty()
        }
    }

    @Test
    fun `searchCoins handles error correctly`() = runTest {
        val query = "bitcoin"
        val errorMessage = "Network error"
        val exception = RuntimeException("Test exception")

        coEvery { repository.searchCoins("") } returns mockSearchResult
        coEvery { repository.getTrendingCoins() } returns SearchTrendingResult(mockTrendingCoins)
        coEvery { repository.searchCoins(query) } throws exception
        every { errorHandler.handleError(any()) } returns errorMessage

        viewModel = SearchViewModel(repository, searchDataStore, errorHandler)

        viewModel.onSearch(query)
        advanceTimeBy(500)

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isEqualTo(errorMessage)
    }

    @Test
    fun `clearHistory removes history items from state`() = runTest {
        coEvery { searchDataStore.clearSearchPreference() } returns Unit
        coEvery { repository.searchCoins(any()) } returns mockSearchResult
        coEvery { repository.getTrendingCoins() } returns SearchTrendingResult(mockTrendingCoins)
        coEvery { searchDataStore.searchQueries } returns flowOf("bitcoin ethereum")

        viewModel = SearchViewModel(repository, searchDataStore, errorHandler)
        viewModel.onSearch("test")
        advanceTimeBy(500)
        advanceUntilIdle()

        viewModel.clearHistory()
        advanceUntilIdle()

        coVerify { searchDataStore.clearSearchPreference() }
        viewModel.uiState.test {
            val state = awaitItem()
            val hasHistory = state.items.any { it is SearchItem.SearchHistory }
            assertThat(hasHistory).isFalse()
        }
    }

    @Test
    fun `buildSearchItemsList includes trending coins when available`() = runTest {
        val query = "bitcoin"
        coEvery { repository.searchCoins(any()) } returns mockSearchResult
        coEvery { repository.getTrendingCoins() } returns SearchTrendingResult(mockTrendingCoins)

        viewModel = SearchViewModel(repository, searchDataStore, errorHandler)
        viewModel.onSearch(query)
        advanceTimeBy(500)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            val hasTrending = state.items.any { it is SearchItem.Trending }
            assertThat(hasTrending).isTrue()
        }
    }
}
