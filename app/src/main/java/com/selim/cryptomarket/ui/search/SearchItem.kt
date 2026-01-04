package com.selim.cryptomarket.ui.search

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.selim.cryptomarket.data.CurrencyResponse
import com.selim.cryptomarket.data.NftResponse
import com.selim.cryptomarket.data.TrendingCoinResponse
import kotlinx.collections.immutable.ImmutableList

@Immutable
sealed class SearchItem {
    @Immutable
    data class Currency(val currencyResponse: CurrencyResponse) : SearchItem()
    
    @Immutable
    data class Nfts(val nfts: ImmutableList<NftResponse>) : SearchItem()
    
    @Immutable
    data class Trending(val trendingResponse: TrendingCoinResponse) : SearchItem()
    
    @Immutable
    data class Title(@StringRes val titleResId: Int) : SearchItem()
    
    @Immutable
    data class SearchHistory(val searchQueries: ImmutableList<String>) : SearchItem()
}
