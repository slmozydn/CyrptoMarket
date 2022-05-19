package com.selim.cryptomarket.service

import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.data.SearchResult
import com.selim.cryptomarket.ui.home.HomeViewModel.Companion.PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoCurrencyService {

    @GET("coins/markets")
    suspend fun fetchCoins(
        @Query("vs_currency") currency: String = "usd",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = PAGE_SIZE
    ): List<CoinResponse>

    @GET("search")
    suspend fun searchCoins(@Query("query") searchQuery: String): SearchResult
}
