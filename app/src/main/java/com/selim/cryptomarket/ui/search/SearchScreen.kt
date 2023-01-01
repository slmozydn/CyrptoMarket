package com.selim.cryptomarket.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.selim.cryptomarket.ui.home.ErrorState
import com.selim.cryptomarket.ui.home.LoadingState
import com.selim.cryptomarket.ui.home.SearchBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController, modifier: Modifier = Modifier) {

    val viewModel = hiltViewModel<SearchViewModel>()
    val uiState = viewModel.uiState.collectAsState().value
    val searchQuery = remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.statusBarsPadding(),
        topBar = {
            Surface(modifier = modifier.fillMaxWidth()) {
                Row(
                    Modifier.background(MaterialTheme.colors.background),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBar(searchQuery, navController, false)
                    Text(
                        modifier = modifier.padding(start = 8.dp),
                        text = "Cancel",
                        style = MaterialTheme.typography.h3,
                        color = Color(0xFFD5A50D)
                    )
                }
            }
        },
        content = {
            LazyColumn(
                modifier = modifier.background(MaterialTheme.colors.onBackground),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                items(uiState) {
                    when (it) {
                        SearchItem.Error -> ErrorState(message = "error")
                        SearchItem.Loading -> LoadingState()
                        is SearchItem.Title -> {}
                        is SearchItem.Nft -> {}
                        is SearchItem.Currency -> {}
                        is SearchItem.Trending -> {}
                    }
                }
            }
        }
    )
}
