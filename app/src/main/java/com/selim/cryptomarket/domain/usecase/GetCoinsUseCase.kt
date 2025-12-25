package com.selim.cryptomarket.domain.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.data.repository.CryptoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(
    private val repository: CryptoRepository,
) {
    operator fun invoke(currencyCode: String): Flow<PagingData<CoinResponse>> {
        return repository.getCoins(currencyCode)
    }
}
