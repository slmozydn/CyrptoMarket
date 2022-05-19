package com.selim.cryptomarket.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.selim.cryptomarket.R
import com.selim.cryptomarket.data.SearchData
import com.selim.cryptomarket.data.SearchData.CurrencyResponse
import com.selim.cryptomarket.data.SearchData.NftResponse
import com.selim.cryptomarket.databinding.ItemCoinBinding
import com.selim.cryptomarket.databinding.ItemNftBinding
import com.selim.cryptomarket.ui.search.SearchViewHolder.CoinViewHolder
import com.selim.cryptomarket.ui.search.SearchViewHolder.NftViewHolder

class SearchAdapter : ListAdapter<SearchData, SearchViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_coin -> CoinViewHolder(ItemCoinBinding.inflate(layoutInflater))
            R.layout.item_nft -> NftViewHolder(ItemNftBinding.inflate(layoutInflater))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is CoinViewHolder -> holder.bind(item as CurrencyResponse)
            is NftViewHolder -> holder.bind(item as NftResponse)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CurrencyResponse -> R.layout.item_coin
            is NftResponse -> R.layout.item_nft
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<SearchData>() {
        override fun areItemsTheSame(oldItem: SearchData, newItem: SearchData) = when {
            oldItem is CurrencyResponse && newItem is CurrencyResponse -> oldItem.id == newItem.id
            oldItem is NftResponse && newItem is NftResponse -> oldItem.id == newItem.id
            else -> false
        }

        override fun areContentsTheSame(oldItem: SearchData, newItem: SearchData) = when {
            oldItem is CurrencyResponse && newItem is CurrencyResponse -> oldItem == newItem
            oldItem is NftResponse && newItem is NftResponse -> oldItem == newItem
            else -> false
        }
    }
}
