package com.selim.cryptomarket.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.selim.cryptomarket.data.CoinChartResponse
import com.selim.cryptomarket.data.CoinDetailResponse
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.data.SearchResult
import com.selim.cryptomarket.data.SearchTrendingResult
import com.selim.cryptomarket.data.paging.CoinsPagingSource
import com.selim.cryptomarket.service.CryptoCurrencyService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val cryptoCurrencyService: CryptoCurrencyService
) : CryptoRepository {

    override fun getCoins(currencyCode: String): Flow<PagingData<CoinResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CoinsPagingSource(cryptoCurrencyService, currencyCode)
            }
        ).flow
    }

    override suspend fun getCoinDetail(id: String): CoinDetailResponse {
        return cryptoCurrencyService.coinDetails(id)
    }

    override suspend fun getCoinChart(
        id: String,
        days: String,
        interval: String
    ): CoinChartResponse {
        return cryptoCurrencyService.coinChart(id, days, interval)
    }

    override suspend fun searchCoins(query: String): SearchResult {
        return cryptoCurrencyService.search(query)
    }

    override suspend fun getTrendingCoins(): SearchTrendingResult {
        return cryptoCurrencyService.searchTrending()
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
