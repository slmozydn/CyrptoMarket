package com.selim.cryptomarket.service

import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.data.SearchResult
import com.selim.cryptomarket.data.SearchTrendingResult
import com.selim.cryptomarket.ui.home.HomeViewModel.Companion.PAGE_SIZE
import retrofit2.http.GET
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
}
