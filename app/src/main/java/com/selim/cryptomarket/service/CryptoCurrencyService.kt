package com.selim.cryptomarket.service

import com.selim.cryptomarket.data.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoCurrencyService {

    @GET("/api/v3/search")
    suspend fun searchCoins(@Query("query") searchQuery: String): SearchResponse
}
