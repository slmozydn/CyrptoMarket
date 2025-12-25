package com.selim.cryptomarket.domain.usecase

import com.selim.cryptomarket.data.CoinDetailResponse
import com.selim.cryptomarket.data.repository.CryptoRepository
import javax.inject.Inject

class GetCoinDetailUseCase @Inject constructor(
    private val repository: CryptoRepository
) {
    suspend operator fun invoke(coinId: String): CoinDetailResponse {
        return repository.getCoinDetail(coinId)
    }
}
