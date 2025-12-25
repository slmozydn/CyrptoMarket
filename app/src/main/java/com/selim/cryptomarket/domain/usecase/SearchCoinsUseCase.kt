package com.selim.cryptomarket.domain.usecase

import com.selim.cryptomarket.data.SearchResult
import com.selim.cryptomarket.data.repository.CryptoRepository
import javax.inject.Inject

class SearchCoinsUseCase @Inject constructor(
    private val repository: CryptoRepository,
) {
    suspend operator fun invoke(query: String): SearchResult {
        return repository.searchCoins(query)
    }
}
