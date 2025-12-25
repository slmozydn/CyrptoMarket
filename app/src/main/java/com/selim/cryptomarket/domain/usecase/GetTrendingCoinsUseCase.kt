package com.selim.cryptomarket.domain.usecase

import com.selim.cryptomarket.data.SearchTrendingResult
import com.selim.cryptomarket.data.repository.CryptoRepository
import javax.inject.Inject

class GetTrendingCoinsUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    suspend operator fun invoke(): SearchTrendingResult {
        return repository.getTrendingCoins()
    }
}
