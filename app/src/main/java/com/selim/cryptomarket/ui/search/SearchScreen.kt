package com.selim.cryptomarket.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.mutableStateOf
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
import com.selim.cryptomarket.ui.home.SearchBar
import com.selim.cryptomarket.util.formatMarketCap
import com.selim.cryptomarket.util.formatScore
import com.selim.cryptomarket.util.formatSymbol
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val uiState = viewModel.uiState.collectAsState().value
    val searchQuery = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.statusBarsPadding(),
        topBar = {
            Surface(
                shadowElevation = 8.dp,
                modifier = modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBar(searchQuery, navController, readOnly = false, viewModel::onSearch) {
                        coroutineScope.launch {
                            viewModel.saveHistory(it)
                        }
                    }

                    Text(
                        modifier = modifier
                            .padding(start = 8.dp)
                            .clickable { navController.navigateUp() },
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
        },
        content = { paddingValues ->
            val backgroundColor = MaterialTheme.colorScheme.surface

            LazyColumn(
                contentPadding = paddingValues,
                modifier = modifier
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                items(uiState) { uiState ->
                    when (uiState) {
                        SearchItem.Error -> ErrorState(backgroundColor = backgroundColor)
                        SearchItem.Loading -> LoadingState(backgroundColor = backgroundColor)
                        is SearchItem.SearchHistory -> SearchHistory(
                            uiState.searchQueries,
                            viewModel::onSearch,
                            viewModel::clearHistory
                        )

                        is SearchItem.Title -> Title(titleResId = uiState.titleResId)
                        is SearchItem.Nfts -> Nfts(nfts = uiState.nfts)
                        is SearchItem.Currency -> Currency(currency = uiState.currencyResponse)
                        is SearchItem.Trending -> Trending(trending = uiState.trendingResponse.trendingCoin)
                    }
                }
            }
        }
    )
}

@Composable
fun Title(titleResId: Int) {
    Text(
        text = stringResource(id = titleResId),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
fun SearchHistory(searchQueries: List<String>, onSearch: (String) -> Unit, onClear: () -> Unit) {

    if (searchQueries.isEmpty()) return

    Title(titleResId = R.string.search_history)

    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        LazyRow {
            items(
                count = searchQueries.size,
                itemContent = { index ->
                    Text(
                        text = searchQueries[index].uppercase(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { onSearch(searchQueries[index]) }
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(6.dp)
                            .requiredWidth(48.dp)
                    )
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    coroutineScope.launch {
                        onClear()
                    }
                },
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun Currency(currency: CurrencyResponse, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        AsyncImage(
            model = currency.large,
            modifier = modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )

        Column(Modifier.padding(start = 16.dp)) {
            Row {
                Text(
                    text = currency.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = currency.symbol.formatSymbol(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = modifier.padding(top = 2.dp, start = 4.dp)
                )
            }

            Text(
                text = currency.marketCapRank.formatMarketCap(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun Trending(trending: Trending, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        AsyncImage(
            model = trending.imageUrl,
            modifier = modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )

        Column(Modifier.padding(start = 16.dp)) {
            Row {
                Text(
                    textAlign = TextAlign.Center,
                    text = trending.score.formatScore(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = Bold),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = modifier
                        .padding(end = 8.dp)
                        .clip(shape = RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .requiredWidth(24.dp)
                )

                Text(
                    text = trending.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = trending.symbol.formatSymbol(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = modifier.padding(top = 2.dp, start = 4.dp, bottom = 4.dp)
                )
            }

            Text(
                text = trending.marketCapRank.formatMarketCap(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun Nfts(nfts: List<NftResponse>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        LazyRow {
            items(
                count = nfts.size,
                itemContent = { index ->
                    Nft(nfts[index])
                }
            )
        }
    }
}

@Composable
fun Nft(nft: NftResponse, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = nft.thumb,
            modifier = modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Column {
            Text(
                text = nft.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = nft.symbol.formatSymbol(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}
