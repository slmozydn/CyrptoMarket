package com.selim.cryptomarket.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.service.CryptoCurrencyService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cryptoCurrencyService: CryptoCurrencyService
) : ViewModel() {

    val coins: Flow<PagingData<CoinResponse>> = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        CoinsPagingSource(cryptoCurrencyService)
    }.flow.cachedIn(viewModelScope)

    companion object {
        const val PAGE_SIZE = 20
    }
}
