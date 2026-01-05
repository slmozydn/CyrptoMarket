package com.selim.cryptomarket.data.repository

import com.google.common.truth.Truth.assertThat
import com.selim.cryptomarket.data.CoinChartResponse
import com.selim.cryptomarket.data.CoinDetailResponse
import com.selim.cryptomarket.data.SearchResult
import com.selim.cryptomarket.data.SearchTrendingResult
import com.selim.cryptomarket.service.CryptoCurrencyService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CryptoRepositoryImplTest {

    private lateinit var service: CryptoCurrencyService
    private lateinit var repository: CryptoRepositoryImpl

    @Before
    fun setup() {
        service = mockk()
        repository = CryptoRepositoryImpl(service)
    }

    @Test
    fun `getCoins returns paging data flow`() = runTest {
        val currencyCode = "usd"

        val result = repository.getCoins(currencyCode)

        assertThat(result).isNotNull()
    }

    @Test
    fun `getCoinDetail returns coin detail response`() = runTest {
        val coinId = "bitcoin"
        val mockDetail = mockk<CoinDetailResponse>(relaxed = true)
        coEvery { service.coinDetails(coinId) } returns mockDetail

        val result = repository.getCoinDetail(coinId)

        assertThat(result).isEqualTo(mockDetail)
        coVerify { service.coinDetails(coinId) }
    }

    @Test
    fun `getCoinChart returns chart response with default parameters`() = runTest {
        val coinId = "bitcoin"
        val mockChart = mockk<CoinChartResponse>(relaxed = true)
        coEvery { service.coinChart(coinId, "30", "daily") } returns mockChart

        val result = repository.getCoinChart(coinId)

        assertThat(result).isEqualTo(mockChart)
        coVerify { service.coinChart(coinId, "30", "daily") }
    }

    @Test
    fun `getCoinChart returns chart response with custom parameters`() = runTest {
        val coinId = "bitcoin"
        val days = "7"
        val interval = "hourly"
        val mockChart = mockk<CoinChartResponse>(relaxed = true)
        coEvery { service.coinChart(coinId, days, interval) } returns mockChart

        val result = repository.getCoinChart(coinId, days, interval)

        assertThat(result).isEqualTo(mockChart)
        coVerify { service.coinChart(coinId, days, interval) }
    }

    @Test
    fun `searchCoins returns search result`() = runTest {
        val query = "bitcoin"
        val mockResult = mockk<SearchResult>(relaxed = true)
        coEvery { service.search(query) } returns mockResult

        val result = repository.searchCoins(query)

        assertThat(result).isEqualTo(mockResult)
        coVerify { service.search(query) }
    }

    @Test
    fun `getTrendingCoins returns trending result`() = runTest {
        val mockResult = mockk<SearchTrendingResult>(relaxed = true)
        coEvery { service.searchTrending() } returns mockResult

        val result = repository.getTrendingCoins()

        assertThat(result).isEqualTo(mockResult)
        coVerify { service.searchTrending() }
    }

    @Test
    fun `getCoinDetail throws exception when service fails`() = runTest {
        val coinId = "bitcoin"
        val exception = RuntimeException("Network error")
        coEvery { service.coinDetails(coinId) } throws exception

        try {
            repository.getCoinDetail(coinId)
            throw AssertionError("Expected exception was not thrown")
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
    }

    @Test
    fun `searchCoins throws exception when service fails`() = runTest {
        val query = "bitcoin"
        val exception = RuntimeException("Network error")
        coEvery { service.search(query) } throws exception

        try {
            repository.searchCoins(query)
            throw AssertionError("Expected exception was not thrown")
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
    }
}
