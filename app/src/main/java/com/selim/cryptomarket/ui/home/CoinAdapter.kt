package com.selim.cryptomarket.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.selim.cryptomarket.R
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.databinding.ItemCoinBinding

class CoinAdapter : PagingDataAdapter<CoinResponse, CoinViewHolder>(DiffCallback) {

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
