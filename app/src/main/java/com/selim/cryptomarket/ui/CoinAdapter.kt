package com.selim.cryptomarket.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.selim.cryptomarket.R
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.databinding.ItemCoinBinding
import com.selim.cryptomarket.ui.CoinAdapter.CoinViewHolder
import com.selim.cryptomarket.util.load
import java.lang.IllegalArgumentException

class CoinAdapter : ListAdapter<CoinResponse, CoinViewHolder>(DiffCallback) {

    class CoinViewHolder(private val binding: ItemCoinBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: CoinResponse) = with(binding) {
            coinNameTextView.text = coin.name
            coinSymbolTextView.text = coin.symbol
            coinImageView.load(coin.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CoinViewHolder(ItemCoinBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val coin = getItem(position)
        holder.bind(coin)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CoinResponse -> R.layout.item_coin
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<CoinResponse>() {
        override fun areItemsTheSame(oldItem: CoinResponse, newItem: CoinResponse) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CoinResponse, newItem: CoinResponse) = oldItem == newItem
    }
}
