package com.selim.cryptomarket.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.selim.cryptomarket.R
import com.selim.cryptomarket.ui.search.SearchItem.Currency
import com.selim.cryptomarket.ui.search.SearchItem.Error
import com.selim.cryptomarket.ui.search.SearchItem.Loading
import com.selim.cryptomarket.ui.search.SearchItem.Nft
import com.selim.cryptomarket.ui.search.SearchItem.Title
import com.selim.cryptomarket.databinding.ItemCoinSearchBinding
import com.selim.cryptomarket.databinding.ItemErrorBinding
import com.selim.cryptomarket.databinding.ItemLoadingBinding
import com.selim.cryptomarket.databinding.ItemNftBinding
import com.selim.cryptomarket.databinding.ItemTitleBinding
import com.selim.cryptomarket.ui.search.SearchViewHolder.CoinViewHolder
import com.selim.cryptomarket.ui.search.SearchViewHolder.ErrorViewHolder
import com.selim.cryptomarket.ui.search.SearchViewHolder.LoadingViewHolder
import com.selim.cryptomarket.ui.search.SearchViewHolder.NftViewHolder
import com.selim.cryptomarket.ui.search.SearchViewHolder.TitleViewHolder

class SearchAdapter : ListAdapter<SearchItem, SearchViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_coin -> CoinViewHolder(ItemCoinSearchBinding.inflate(layoutInflater))
            R.layout.item_nft -> NftViewHolder(ItemNftBinding.inflate(layoutInflater))
            R.layout.item_title -> TitleViewHolder(ItemTitleBinding.inflate(layoutInflater))
            R.layout.item_loading -> LoadingViewHolder(ItemLoadingBinding.inflate(layoutInflater))
            R.layout.item_error -> ErrorViewHolder(ItemErrorBinding.inflate(layoutInflater))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is CoinViewHolder -> holder.bind(item as Currency)
            is NftViewHolder -> holder.bind(item as Nft)
            is TitleViewHolder -> holder.bind(item as Title)
            is LoadingViewHolder -> {}
            is ErrorViewHolder -> {}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Loading -> R.layout.item_loading
            is Error -> R.layout.item_error
            is Currency -> R.layout.item_coin
            is Nft -> R.layout.item_nft
            is Title -> R.layout.item_title
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<SearchItem>() {
        override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem) = when {
            oldItem is Currency && newItem is Currency -> oldItem.currencyResponse.id == newItem.currencyResponse.id
            oldItem is Nft && newItem is Nft -> oldItem.nftResponse.id == newItem.nftResponse.id
            else -> false
        }

        override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem) = when {
            oldItem is Currency && newItem is Currency -> oldItem == newItem
            oldItem is Nft && newItem is Nft -> oldItem == newItem
            else -> false
        }
    }
}
