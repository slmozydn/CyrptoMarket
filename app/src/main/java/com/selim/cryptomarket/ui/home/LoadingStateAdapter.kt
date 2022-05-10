package com.selim.cryptomarket.ui.home

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = LoadingStateViewHolder(parent)

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) = holder.bind(loadState, retry)
}
