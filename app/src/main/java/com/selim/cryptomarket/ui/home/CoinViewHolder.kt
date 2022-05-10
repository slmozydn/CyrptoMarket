package com.selim.cryptomarket.ui.home

import androidx.recyclerview.widget.RecyclerView
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.databinding.ItemCoinBinding
import com.selim.cryptomarket.util.load

class CoinViewHolder(private val binding: ItemCoinBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(coin: CoinResponse?) = with(binding) {
        if (coin == null) return@with

        coinNameTextView.text = coin.name
        coinSymbolTextView.text = coin.symbol
        coinImageView.load(coin.image)
    }
}
