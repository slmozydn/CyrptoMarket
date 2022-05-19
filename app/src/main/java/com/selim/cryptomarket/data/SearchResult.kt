package com.selim.cryptomarket.data

import com.selim.cryptomarket.data.SearchData.CurrencyResponse
import com.selim.cryptomarket.data.SearchData.NftResponse

data class SearchResult(val coins: List<CurrencyResponse>, val nfts: List<NftResponse>)
