package com.selim.cryptomarket.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.service.CryptoCurrencyService

class CoinsPagingSource(
    private val service: CryptoCurrencyService,
    private val currencyCode: String,
) : PagingSource<Int, CoinResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinResponse> {
        val pageNumber = params.key ?: 1

        return try {
            val result = service.fetchCoins(page = pageNumber).map {
                it.copy(currencyCode = currencyCode)
            }

            LoadResult.Page(
                data = result,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if (result.isEmpty()) null else pageNumber + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CoinResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }
}
