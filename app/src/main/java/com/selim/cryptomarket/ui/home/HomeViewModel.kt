package com.selim.cryptomarket.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.domain.usecase.GetCoinsUseCase
import com.selim.cryptomarket.ui.settings.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    dataStore: SettingsDataStore,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val coins: Flow<PagingData<CoinResponse>> = dataStore.currencyCode
        .distinctUntilChanged()
        .flatMapLatest { currencyCode ->
            getCoinsUseCase(currencyCode)
        }
        .cachedIn(viewModelScope)

    companion object {
        const val PAGE_SIZE = 20
    }
}
