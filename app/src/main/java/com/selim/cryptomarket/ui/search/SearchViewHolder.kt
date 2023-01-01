package com.selim.cryptomarket.ui.search

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.selim.cryptomarket.databinding.ItemCoinSearchBinding
import com.selim.cryptomarket.databinding.ItemErrorBinding
import com.selim.cryptomarket.databinding.ItemLoadingBinding
import com.selim.cryptomarket.databinding.ItemNftBinding
import com.selim.cryptomarket.databinding.ItemTitleBinding
import com.selim.cryptomarket.databinding.ItemTrendingBinding
import com.selim.cryptomarket.ui.search.SearchItem.Currency
import com.selim.cryptomarket.ui.search.SearchItem.Nfts
import com.selim.cryptomarket.ui.search.SearchItem.Title
import com.selim.cryptomarket.ui.search.SearchItem.Trending
import com.selim.cryptomarket.util.formatMarketCap
import com.selim.cryptomarket.util.formatScore
import com.selim.cryptomarket.util.formatSymbol
import com.selim.cryptomarket.util.load

sealed class SearchViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class LoadingViewHolder(binding: ItemLoadingBinding) : SearchViewHolder(binding)

    class ErrorViewHolder(binding: ItemErrorBinding) : SearchViewHolder(binding)

    class TitleViewHolder(private val binding: ItemTitleBinding) : SearchViewHolder(binding) {
        fun bind(title: Title) = with(binding) {
            titleTextView.text = root.context.getString(title.titleResId)
        }
    }

    class CoinViewHolder(private val binding: ItemCoinSearchBinding) : SearchViewHolder(binding) {
        fun bind(coin: Currency) = with(binding) {
            coinImageView.load(coin.currencyResponse.large)
            coinNameTextView.text = coin.currencyResponse.name
            coinSymbolTextView.text = coin.currencyResponse.symbol.formatSymbol()
            marketCapTextView.text = coin.currencyResponse.marketCapRank.formatMarketCap()
        }
    }

    class NftViewHolder(private val binding: ItemNftBinding) : SearchViewHolder(binding) {
        fun bind(nft: Nfts) = with(binding) {
            // nftImageView.load(nft.nftResponse.thumb)
            // nftNameTextView.text = nft.nftResponse.name
            // nftSymbolTextView.text = nft.nftResponse.symbol
        }
    }

    class TrendingViewHolder(private val binding: ItemTrendingBinding) : SearchViewHolder(binding) {
        fun bind(trending: Trending) = with(binding) {
            val item = trending.trendingResponse.trendingCoin
            coinImageView.load(item.imageUrl)
            coinNameTextView.text = item.name
            coinSymbolTextView.text = item.symbol.formatSymbol()
            marketCapTextView.text = item.marketCapRank.formatMarketCap()
            scoreTextView.text = item.score.formatScore()
        }
    }
}
