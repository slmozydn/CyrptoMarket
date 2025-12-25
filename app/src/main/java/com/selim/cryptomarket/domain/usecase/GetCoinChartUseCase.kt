package com.selim.cryptomarket.domain.usecase

import com.selim.cryptomarket.data.CoinChartResponse
import com.selim.cryptomarket.data.repository.CryptoRepository
import javax.inject.Inject

class GetCoinChartUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    suspend operator fun invoke(
        coinId: String,
        days: String = "30",
        interval: String = "daily"
    ): CoinChartResponse {
        return repository.getCoinChart(coinId, days, interval)
    }
}
