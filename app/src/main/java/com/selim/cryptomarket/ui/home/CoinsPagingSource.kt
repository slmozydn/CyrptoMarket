package com.selim.cryptomarket.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.service.CryptoCurrencyService

class CoinsPagingSource(private val service: CryptoCurrencyService, private val currencyCode: String) :
    PagingSource<Int, CoinResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinResponse> {
        val pageNumber = params.key ?: 1
        return try {
            val result = service.fetchCoins(page = pageNumber).map {
                it.copy(currencyCode = currencyCode)
            }
            val nextPageNumber = if (result.isEmpty()) {
                null
            } else {
                pageNumber + 1
            }

            LoadResult.Page(
                data = result,
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CoinResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
