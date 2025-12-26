package com.selim.cryptomarket.di

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class CurrencyInterceptor(
    private val currencyProvider: CurrencyProvider
) : Interceptor {

    override fun intercept(chain: Chain): Response {
        val request = chain.request()

        val currencyCode = currencyProvider.getCurrentCurrency()

        val url = request.url.newBuilder()
            .addQueryParameter(CURRENCY_KEY, currencyCode)
            .build()

        val newRequest = request.newBuilder()
            .url(url)
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val CURRENCY_KEY = "vs_currency"
    }
}
