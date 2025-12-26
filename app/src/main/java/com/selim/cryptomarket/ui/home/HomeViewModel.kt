package com.selim.cryptomarket.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.selim.cryptomarket.data.repository.CryptoRepository
import com.selim.cryptomarket.ui.home.CoinUiModelMapper.mapToUiModel
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CryptoRepository,
    dataStore: SettingsDataStore,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val coins: Flow<PagingData<CoinUiModel>> = dataStore.currencyCode
        .distinctUntilChanged()
        .flatMapLatest { currencyCode ->
            repository.getCoins(currencyCode)
        }
        .map { pagingData ->
            pagingData.map() { coinResponse ->
                coinResponse.mapToUiModel()
            }
        }
        .cachedIn(viewModelScope)

    companion object {
        const val PAGE_SIZE = 20
    }
}
