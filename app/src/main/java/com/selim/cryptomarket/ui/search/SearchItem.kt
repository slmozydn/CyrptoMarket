package com.selim.cryptomarket.ui.search

import androidx.annotation.StringRes
import com.selim.cryptomarket.data.CurrencyResponse
import com.selim.cryptomarket.data.NftResponse
import com.selim.cryptomarket.data.TrendingCoinResponse

sealed class SearchItem {
    data class Currency(val currencyResponse: CurrencyResponse) : SearchItem()
    data class Nft(val nftResponse: NftResponse) : SearchItem()
    data class Trending(val trendingResponse: TrendingCoinResponse) : SearchItem()
    data class Title(@StringRes val titleResId: Int) : SearchItem()
    object Loading : SearchItem()
    object Error : SearchItem()
}
