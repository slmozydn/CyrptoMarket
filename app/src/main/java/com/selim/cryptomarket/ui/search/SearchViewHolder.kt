package com.selim.cryptomarket.ui.search

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.selim.cryptomarket.ui.search.SearchItem.Currency
import com.selim.cryptomarket.ui.search.SearchItem.Nft
import com.selim.cryptomarket.ui.search.SearchItem.Title
import com.selim.cryptomarket.databinding.ItemCoinBinding
import com.selim.cryptomarket.databinding.ItemErrorBinding
import com.selim.cryptomarket.databinding.ItemLoadingBinding
import com.selim.cryptomarket.databinding.ItemNftBinding
import com.selim.cryptomarket.databinding.ItemTitleBinding
import com.selim.cryptomarket.util.load

sealed class SearchViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class LoadingViewHolder(binding: ItemLoadingBinding) : SearchViewHolder(binding)

    class ErrorViewHolder(binding: ItemErrorBinding) : SearchViewHolder(binding)

    class TitleViewHolder(private val binding: ItemTitleBinding) : SearchViewHolder(binding) {
        fun bind(title: Title) {
            binding.titleTextView.text = title.titleText
        }
    }

    class CoinViewHolder(private val binding: ItemCoinBinding) : SearchViewHolder(binding) {
        fun bind(coin: Currency) = with(binding) {
            coinImageView.load(coin.currencyResponse.large)
            coinNameTextView.text = coin.currencyResponse.name
            coinSymbolTextView.text = coin.currencyResponse.symbol
        }
    }

    class NftViewHolder(private val binding: ItemNftBinding) : SearchViewHolder(binding) {
        fun bind(nft: Nft) = with(binding) {
            nftImageView.load(nft.nftResponse.thumb)
            nftNameTextView.text = nft.nftResponse.name
            nftSymbolTextView.text = nft.nftResponse.symbol
        }
    }
}
