package com.selim.cryptomarket.ui.search

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.selim.cryptomarket.data.SearchData.NftResponse
import com.selim.cryptomarket.data.SearchData.CurrencyResponse
import com.selim.cryptomarket.databinding.ItemCoinBinding
import com.selim.cryptomarket.databinding.ItemNftBinding
import com.selim.cryptomarket.util.load

sealed class SearchViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class CoinViewHolder(private val binding: ItemCoinBinding) : SearchViewHolder(binding) {
        fun bind(coin: CurrencyResponse) = with(binding) {
            coinImageView.load(coin.large)
            coinNameTextView.text = coin.name
            coinSymbolTextView.text = coin.symbol
        }
    }

    class NftViewHolder(private val binding: ItemNftBinding) : SearchViewHolder(binding) {
        fun bind(nft: NftResponse) = with(binding) {
            nftImageView.load(nft.thumb)
            nftNameTextView.text = nft.name
            nftSymbolTextView.text = nft.symbol
        }
    }
}
