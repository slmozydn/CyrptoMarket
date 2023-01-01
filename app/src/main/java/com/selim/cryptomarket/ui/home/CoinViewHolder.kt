package com.selim.cryptomarket.ui.home

import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.selim.cryptomarket.R
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.databinding.ItemCoinBinding
import com.selim.cryptomarket.util.formatPercentage
import com.selim.cryptomarket.util.formatPrice
import com.selim.cryptomarket.util.formatSymbol
import com.selim.cryptomarket.util.formatVolume
import com.selim.cryptomarket.util.load

class CoinViewHolder(private val binding: ItemCoinBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(coin: CoinResponse?) = with(binding) {
        if (coin == null) return@with

        coinImageView.load(coin.image)
        coinNameTextView.text = coin.name
        coinSymbolTextView.text = coin.symbol.formatSymbol()
        //priceTextView.text = coin.currentPrice.formatPrice(coin.currencyCode)
        volumeTextView.text = coin.totalVolume.formatVolume()
        renderPercentage(coin.priceChangePercentage24h)
    }

    private fun renderPercentage(percentage: Double?) {
        val percentageBackgroundRes = when {
            percentage == null || percentage == 0.0 -> R.drawable.background_gray
            percentage > 0 -> R.drawable.background_green
            else -> R.drawable.background_watermelon
        }
        binding.priceChangeTextView.apply {
            text = percentage.formatPercentage()
            background = AppCompatResources.getDrawable(this.context, percentageBackgroundRes)
        }
    }
}
