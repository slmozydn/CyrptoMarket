package com.selim.cryptomarket.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.selim.cryptomarket.R
import com.selim.cryptomarket.data.CurrencyResponse
import com.selim.cryptomarket.data.NftResponse
import com.selim.cryptomarket.data.Trending
import com.selim.cryptomarket.ui.home.ErrorState
import com.selim.cryptomarket.ui.home.LoadingState
import com.selim.cryptomarket.ui.navigation.Screen
import com.selim.cryptomarket.util.formatMarketCap
import com.selim.cryptomarket.util.formatScore
import com.selim.cryptomarket.util.formatSymbol
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.statusBarsPadding(),
        topBar = {
            SearchTopBar(
                searchQuery = uiState.searchQuery,
                onQueryChange = viewModel::onSearch,
                saveHistory = { query ->
                    coroutineScope.launch {
                        viewModel.saveHistory(query)
                    }
                },
                onCancel = { navController.navigateUp() },
                modifier = modifier,
            )
        },
        content = { paddingValues ->
            SearchContent(
                uiState = uiState,
                onSearch = viewModel::onSearch,
                onClearHistory = viewModel::clearHistory,
                onCoinClick = { coinId ->
                    navController.navigate(Screen.Detail(coinId = coinId))
                },
                paddingValues = paddingValues,
                modifier = modifier,
            )
        },
    )
}

@Composable
private fun SearchTopBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    saveHistory: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shadowElevation = 4.dp,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SearchBar(
                searchQuery = searchQuery,
                readOnly = false,
                onQueryChange = onQueryChange,
                onSearchClick = {},
                saveHistory = saveHistory,
            )

            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable(onClick = onCancel),
                text = stringResource(id = R.string.cancel),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primaryContainer,
            )
        }
    }
}

@Composable
private fun SearchContent(
    uiState: SearchViewModel.SearchUiState,
    onSearch: (String) -> Unit,
    onClearHistory: () -> Unit,
    onCoinClick: (String) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> {
            LoadingState()
        }

        uiState.errorMessage != null -> {
            ErrorState(message = uiState.errorMessage)
        }

        else -> {
            SearchItemsList(
                items = uiState.items,
                onSearch = onSearch,
                onClearHistory = onClearHistory,
                onCoinClick = onCoinClick,
                paddingValues = paddingValues,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun SearchItemsList(
    items: List<SearchItem>,
    onSearch: (String) -> Unit,
    onClearHistory: () -> Unit,
    onCoinClick: (String) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = paddingValues,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        items(
            items = items,
            key = { item -> item.hashCode() },
        ) { item ->
            when (item) {
                is SearchItem.SearchHistory -> SearchHistory(
                    searchQueries = item.searchQueries,
                    onSearch = onSearch,
                    onClear = onClearHistory,
                )

                is SearchItem.Title -> Title(titleResId = item.titleResId)
                is SearchItem.Nfts -> Nfts(nfts = item.nfts)
                is SearchItem.Currency -> Currency(
                    currency = item.currencyResponse,
                    onCoinClick = onCoinClick,
                )

                is SearchItem.Trending -> Trending(
                    trending = item.trendingResponse.trendingCoin,
                    onCoinClick = onCoinClick,
                )
            }
        }
    }
}

@Composable
fun Title(titleResId: Int) {
    Text(
        text = stringResource(id = titleResId),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 16.dp),
    )
}

@Composable
private fun SearchHistory(
    searchQueries: List<String>,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
) {
    if (searchQueries.isEmpty()) return

    Title(titleResId = R.string.search_history)

    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        LazyRow {
            items(
                count = searchQueries.size,
                key = { index -> "${searchQueries[index]}_$index" },
            ) { index ->
                val query = searchQueries[index]
                val upperQuery = remember(query) { query.uppercase() }

                Text(
                    text = upperQuery,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { onSearch(query) }
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(6.dp)
                        .width(48.dp),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = "Clear history",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    coroutineScope.launch {
                        onClear()
                    }
                },
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun Currency(
    currency: CurrencyResponse,
    onCoinClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val formattedSymbol = remember(currency.symbol) {
        currency.symbol.formatSymbol()
    }

    val formattedMarketCap = remember(currency.marketCapRank) {
        currency.marketCapRank?.formatMarketCap().orEmpty()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onCoinClick(currency.id)
            }
            .padding(vertical = 8.dp),
    ) {
        AsyncImage(
            model = currency.large,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit,
            contentDescription = "${currency.name} icon",
        )

        Column(Modifier.padding(start = 16.dp)) {
            Row {
                Text(
                    text = currency.name,
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = formattedSymbol,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(top = 2.dp, start = 4.dp),
                )
            }

            Text(
                text = formattedMarketCap,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun Trending(
    trending: Trending,
    onCoinClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val formattedScore = remember(trending.score) {
        trending.score.formatScore()
    }

    val formattedSymbol = remember(trending.symbol) {
        trending.symbol.formatSymbol()
    }

    val formattedMarketCap = remember(trending.marketCapRank) {
        trending.marketCapRank.formatMarketCap()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onCoinClick(trending.id)
            }
            .padding(vertical = 8.dp),
    ) {
        AsyncImage(
            model = trending.imageUrl,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit,
            contentDescription = "${trending.name} icon",
        )

        Column(Modifier.padding(start = 16.dp)) {
            Row {
                Text(
                    textAlign = TextAlign.Center,
                    text = formattedScore,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = Bold),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(shape = RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .requiredWidth(24.dp),
                )

                Text(
                    text = trending.name,
                    style = MaterialTheme.typography.titleMedium,
                )

                Text(
                    text = formattedSymbol,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(top = 2.dp, start = 4.dp, bottom = 4.dp),
                )
            }

            Text(
                text = formattedMarketCap,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun Nfts(nfts: List<NftResponse>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) {
        LazyRow {
            items(
                items = nfts,
                key = { nft -> nft.id },
            ) { nft ->
                Nft(nft = nft)
            }
        }
    }
}

@Composable
private fun Nft(nft: NftResponse, modifier: Modifier = Modifier) {
    val formattedSymbol = remember(nft.symbol) {
        nft.symbol.formatSymbol()
    }

    Row(
        modifier = modifier.padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = nft.thumb,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = "${nft.name} icon",
        )

        Column {
            Text(
                text = nft.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Text(
                text = formattedSymbol,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            )
        }
    }
}
