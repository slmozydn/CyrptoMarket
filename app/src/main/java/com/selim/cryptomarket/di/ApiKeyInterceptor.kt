package com.selim.cryptomarket.di

import com.selim.cryptomarket.BuildConfig
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        val apiKey = BuildConfig.COINGECKO_API_KEY
        val newRequest = if (apiKey.isNotEmpty()) {
            request.newBuilder()
                .addHeader(API_KEY_HEADER, apiKey)
                .build()
        } else {
            request
        }

        return chain.proceed(newRequest)
    }

    companion object {
        private const val API_KEY_HEADER = "x-cg-demo-api-key"
    }
}
