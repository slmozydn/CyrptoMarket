package com.selim.cryptomarket.data.paging

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.service.CryptoCurrencyService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CoinsPagingSourceTest {

    private lateinit var service: CryptoCurrencyService
    private lateinit var pagingSource: CoinsPagingSource

    private val mockCoins = listOf(
        CoinResponse(
            id = "bitcoin",
            symbol = "btc",
            name = "Bitcoin",
            image = "image_url",
            currentPrice = 45000.0,
            marketCap = 850000000000.0,
            marketCapRank = 1,
            totalVolume = 25000000000.0,
            priceChangePercentage24h = 2.5
        ),

        CoinResponse(
            id = "ethereum",
            symbol = "eth",
            name = "Ethereum",
            image = "image_url",
            currentPrice = 3000.0,
            marketCap = 360000000000.0,
            marketCapRank = 2,
            totalVolume = 15000000000.0,
            priceChangePercentage24h = 1.8
        )
    )

    @Before
    fun setup() {
        service = mockk()
        pagingSource = CoinsPagingSource(service, "usd")
    }

    @Test
    fun `load returns page when successful`() = runTest {
        coEvery { service.fetchCoins(page = 1) } returns mockCoins

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).hasSize(2)
        assertThat(page.data[0].id).isEqualTo("bitcoin")
        assertThat(page.data[0].currencyCode).isEqualTo("usd")
        assertThat(page.data[1].id).isEqualTo("ethereum")
        assertThat(page.prevKey).isNull()
        assertThat(page.nextKey).isEqualTo(2)
    }

    @Test
    fun `load returns page with prev and next keys for middle page`() = runTest {
        coEvery { service.fetchCoins(page = 2) } returns mockCoins

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 2,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        val page = result as PagingSource.LoadResult.Page
        assertThat(page.prevKey).isEqualTo(1)
        assertThat(page.nextKey).isEqualTo(3)
    }

    @Test
    fun `load returns page with null next key when data is empty`() = runTest {
        coEvery { service.fetchCoins(page = 1) } returns emptyList()

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        val page = result as PagingSource.LoadResult.Page
        assertThat(page.data).isEmpty()
        assertThat(page.prevKey).isNull()
        assertThat(page.nextKey).isNull()
    }

    @Test
    fun `load returns error when Exception occurs`() = runTest {
        val exception = IOException()
        coEvery { service.fetchCoins(any()) } throws exception

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        val error = result as PagingSource.LoadResult.Error
        assertThat(error.throwable).isEqualTo(exception)
    }
}
