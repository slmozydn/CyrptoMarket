package com.selim.cryptomarket.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.selim.cryptomarket.R
import com.selim.cryptomarket.databinding.ItemLoadingStateBinding

class LoadingStateViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_loading_state, parent, false)
) {
    private val binding = ItemLoadingStateBinding.bind(itemView)

    fun bind(loadState: LoadState, retry: () -> Unit) = with(binding) {
        if (loadState is LoadState.Error) {
            errorMsg.text = loadState.error.localizedMessage
        }
        retryButton.apply {
            setOnClickListener { retry() }
            isVisible = loadState is LoadState.Error
        }
        errorMsg.isVisible = loadState is LoadState.Error
        progressBar.isVisible = loadState is LoadState.Loading
    }
}
