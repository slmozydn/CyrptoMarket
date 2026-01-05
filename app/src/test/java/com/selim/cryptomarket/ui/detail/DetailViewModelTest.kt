package com.selim.cryptomarket.ui.detail

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.selim.cryptomarket.data.CoinChartResponse
import com.selim.cryptomarket.data.CoinDetailResponse
import com.selim.cryptomarket.data.repository.CryptoRepository
import com.selim.cryptomarket.ui.settings.CurrencyType
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import com.selim.cryptomarket.util.ErrorHandler
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private lateinit var repository: CryptoRepository
    private lateinit var settingsDataStore: SettingsDataStore
    private lateinit var errorHandler: ErrorHandler
    private lateinit var viewModel: DetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val mockDetailResponse = mockk<CoinDetailResponse>(relaxed = true)
    private val mockChartResponse = mockk<CoinChartResponse>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        settingsDataStore = mockk()
        errorHandler = mockk()

        every { settingsDataStore.currencyCode } returns flowOf(CurrencyType.USD.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() {
        viewModel = DetailViewModel(repository, settingsDataStore, errorHandler)

        val state = viewModel.uiState.value
        assertThat(state.loading).isTrue()
        assertThat(state.displayModel).isNull()
        assertThat(state.errorMessage).isNull()
        assertThat(state.isRefreshing).isFalse()
    }

    @Test
    fun `getDetail updates state with success`() = runTest {
        viewModel = DetailViewModel(repository, settingsDataStore, errorHandler)
        val coinId = "bitcoin"
        coEvery { repository.getCoinDetail(coinId) } returns mockDetailResponse
        coEvery { repository.getCoinChart(coinId, any()) } returns mockChartResponse

        viewModel.getDetail(coinId)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.isRefreshing).isFalse()
            assertThat(state.displayModel).isNotNull()
            assertThat(state.errorMessage).isNull()
        }
    }

    @Test
    fun `getDetail sets isRefreshing to true during loading`() = runTest {
        viewModel = DetailViewModel(repository, settingsDataStore, errorHandler)
        val coinId = "bitcoin"
        coEvery { repository.getCoinDetail(coinId) } coAnswers {
            kotlinx.coroutines.delay(100)
            mockDetailResponse
        }
        coEvery { repository.getCoinChart(coinId, any()) } returns mockChartResponse

        viewModel.uiState.test {
            skipItems(1) // Skip initial state
            viewModel.getDetail(coinId)

            val loadingState = awaitItem()
            assertThat(loadingState.isRefreshing).isTrue()

            advanceUntilIdle()
            val successState = awaitItem()
            assertThat(successState.isRefreshing).isFalse()
        }
    }

    @Test
    fun `getDetail handles error correctly`() = runTest {
        viewModel = DetailViewModel(repository, settingsDataStore, errorHandler)
        val coinId = "bitcoin"
        val errorMessage = "Network error"
        val exception = RuntimeException("Test exception")

        coEvery { repository.getCoinDetail(coinId) } throws exception
        every { errorHandler.handleError(any()) } returns errorMessage

        viewModel.getDetail(coinId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.loading).isFalse()
        assertThat(state.isRefreshing).isFalse()
        assertThat(state.errorMessage).isEqualTo(errorMessage)
        assertThat(state.displayModel).isNull()
    }

    @Test
    fun `onTimeRangeChange updates chart successfully`() = runTest {
        viewModel = DetailViewModel(repository, settingsDataStore, errorHandler)
        val coinId = "bitcoin"
        val newTimeRange = TimeRange.SEVEN_DAYS

        coEvery { repository.getCoinDetail(coinId) } returns mockDetailResponse
        coEvery { repository.getCoinChart(coinId, any()) } returns mockChartResponse

        viewModel.getDetail(coinId)
        advanceUntilIdle()

        viewModel.onTimeRangeChange(coinId, newTimeRange)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.timeRange).isEqualTo(newTimeRange)
            assertThat(state.loading).isFalse()
            assertThat(state.errorMessage).isNull()
        }
    }

    @Test
    fun `default time range is THIRTY_DAYS`() {
        viewModel = DetailViewModel(repository, settingsDataStore, errorHandler)

        assertThat(viewModel.uiState.value.timeRange).isEqualTo(TimeRange.THIRTY_DAYS)
    }
}
