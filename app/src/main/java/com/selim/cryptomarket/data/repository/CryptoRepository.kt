package com.selim.cryptomarket.data.repository

import androidx.paging.PagingData
import com.selim.cryptomarket.data.CoinChartResponse
import com.selim.cryptomarket.data.CoinDetailResponse
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.data.SearchResult
import com.selim.cryptomarket.data.SearchTrendingResult
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    fun getCoins(currencyCode: String): Flow<PagingData<CoinResponse>>

    suspend fun getCoinDetail(id: String): CoinDetailResponse

    suspend fun getCoinChart(
        id: String,
        days: String = "30",
        interval: String = "daily"
    ): CoinChartResponse

    suspend fun searchCoins(query: String): SearchResult

    suspend fun getTrendingCoins(): SearchTrendingResult
}
