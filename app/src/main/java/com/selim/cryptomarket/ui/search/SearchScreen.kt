package com.selim.cryptomarket.ui.search

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
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
                elevation = 8.dp,
                modifier = modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .background(MaterialTheme.colors.background)
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
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.surface
                    )
                }
            }
        },
        content = {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = modifier
                    .background(MaterialTheme.colors.onBackground)
                    .fillMaxSize()
            ) {
                items(uiState) {
                    when (it) {
                        SearchItem.Error -> ErrorState(message = "error")
                        SearchItem.Loading -> LoadingState()
                        is SearchItem.SearchHistory -> SearchHistory(it.searchQueries, viewModel::onSearch)
                        is SearchItem.Title -> Title(it)
                        is SearchItem.Nfts -> Nfts(it.nfts)
                        is SearchItem.Currency -> Currency(it.currencyResponse)
                        is SearchItem.Trending -> Trending(trending = it.trendingResponse.trendingCoin)
                    }
                }
            }
        }
    )
}

@Composable
fun Title(title: SearchItem.Title) {
    Text(
        text = stringResource(id = title.titleResId),
        style = MaterialTheme.typography.h2,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun SearchHistory(searchQueries: List<String>, onSearch: (String) -> Unit) {
    val colors = MaterialTheme.colors

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow {
            items(
                count = searchQueries.size,
                itemContent = { index ->
                    Text(
                        text = searchQueries[index].uppercase(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.subtitle2.copy(color = colors.primaryVariant),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { onSearch(searchQueries[index]) }
                            .drawBehind {
                                drawRoundRect(
                                    cornerRadius = CornerRadius(10f, 10f),
                                    color = colors.secondaryVariant
                                )
                            }
                            .padding(6.dp)
                            .requiredWidth(48.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun Currency(currency: CurrencyResponse, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.onBackground)
            .padding(vertical = 8.dp)
    ) {
        AsyncImage(
            model = currency.large,
            modifier = modifier.size(44.dp),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
        Column(Modifier.padding(start = 16.dp)) {
            Row {
                Text(
                    text = currency.name,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = currency.symbol.formatSymbol(),
                    style = MaterialTheme.typography.caption,
                    modifier = modifier.padding(top = 2.dp, start = 4.dp)
                )
            }
            Text(
                text = currency.marketCapRank.formatMarketCap(),
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun Trending(trending: Trending, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colors

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(colors.onBackground)
            .padding(vertical = 8.dp)
    ) {
        AsyncImage(
            model = trending.imageUrl,
            modifier = modifier.size(44.dp),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
        Column(Modifier.padding(start = 16.dp)) {
            Row {
                Text(
                    textAlign = TextAlign.Center,
                    text = trending.score.formatScore(),
                    style = MaterialTheme.typography.caption.copy(fontWeight = Bold),
                    color = colors.surface,
                    modifier = modifier
                        .padding(end = 8.dp)
                        .drawBehind {
                            drawRoundRect(
                                cornerRadius = CornerRadius(4f, 4f),
                                color = colors.secondaryVariant
                            )
                        }
                        .requiredWidth(24.dp)
                )

                Text(
                    text = trending.name,
                    style = MaterialTheme.typography.subtitle1
                )

                Text(
                    text = trending.symbol.formatSymbol(),
                    style = MaterialTheme.typography.caption,
                    modifier = modifier.padding(top = 2.dp, start = 4.dp)
                )
            }
            Text(
                text = trending.marketCapRank.formatMarketCap(),
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun Nfts(nfts: List<NftResponse>) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.background(MaterialTheme.colors.onBackground)
    ) {
        AsyncImage(
            model = nft.thumb,
            modifier = modifier
                .size(44.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
        Column {
            Text(
                text = nft.name,
                style = MaterialTheme.typography.subtitle1,
                modifier = modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = nft.symbol.formatSymbol(),
                style = MaterialTheme.typography.caption,
                modifier = modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
