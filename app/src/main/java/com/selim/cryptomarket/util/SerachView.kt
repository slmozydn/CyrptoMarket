package com.selim.cryptomarket.util

import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive

@ExperimentalCoroutinesApi
fun SearchView.textChanges(): Flow<CharSequence> = channelFlow {
    trySend(query)
    setOnQueryTextListener(object : OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            if (isActive) {
                this@channelFlow.trySend(newText)
                return true
            }
            return false
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            return false
        }
    })
    awaitClose { setOnQueryTextListener(null) }
}
