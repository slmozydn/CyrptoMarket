package com.selim.cryptomarket.service

import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.data.SearchResult
import com.selim.cryptomarket.data.SearchTrendingResult
import com.selim.cryptomarket.data.CoinChartResponse
import com.selim.cryptomarket.data.CoinDetailResponse
import com.selim.cryptomarket.ui.home.HomeViewModel.Companion.PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoCurrencyService {

    @GET("coins/markets")
    suspend fun fetchCoins(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = PAGE_SIZE
    ): List<CoinResponse>

    @GET("search")
    suspend fun search(@Query("query") searchQuery: String): SearchResult

    @GET("search/trending")
    suspend fun searchTrending(): SearchTrendingResult

    @GET("coins/{id}")
    suspend fun coinDetails(@Path("id") id: String): CoinDetailResponse

    @GET("coins/{id}/market_chart")
    suspend fun coinChart(
        @Path("id") id: String,
        @Query("days") days: String = "30",
        @Query("interval") interval: String = "daily"
    ): CoinChartResponse
}
