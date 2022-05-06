package com.selim.cryptomarket.service

import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.data.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoCurrencyService {

    @GET("coins/markets")
    suspend fun fetchCoins(@Query("vs_currency") currency: String = "usd"): List<CoinResponse>

    @GET("search")
    suspend fun searchCoins(@Query("query") searchQuery: String): SearchResponse
}
