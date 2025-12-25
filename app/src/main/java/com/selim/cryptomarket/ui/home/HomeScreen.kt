package com.selim.cryptomarket.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.draw.clip
import com.selim.cryptomarket.R
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.ui.navigation.Screen
import com.selim.cryptomarket.ui.theme.ThemeViewModel
import com.selim.cryptomarket.util.formatPercentage
import com.selim.cryptomarket.util.formatPrice
import com.selim.cryptomarket.util.formatSymbol
import com.selim.cryptomarket.util.formatVolume

private const val MAX_SEARCH_LENGTH = 20

@Composable
fun HomeScreen(navController: NavController, themeViewModel: ThemeViewModel, isDarkTheme: Boolean) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val coins = homeViewModel.coins.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            Surface(modifier = Modifier.fillMaxWidth()) {
                MainAppBar(navController, themeViewModel, isDarkTheme)
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (coins.loadState.refresh) {
                    is LoadState.Loading -> LoadingState()
                    is LoadState.Error -> ErrorState()
                    else -> CoinList(navController, coins)
                }
            }
        },
    )
}

@Composable
private fun MainAppBar(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    isDarkTheme: Boolean,
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SearchBar(searchQuery = null, navController, readOnly = true)

        val icon = if (isDarkTheme) R.drawable.icon_sun else R.drawable.icon_night

        IconButton(onClick = { themeViewModel.onChangeTheme() }) {
            Icon(
                painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
            )
        }

        IconButton(onClick = { navController.navigate(Screen.Settings) }) {
            Icon(
                painterResource(R.drawable.icon_settings),
                modifier = Modifier.size(26.dp),
                contentDescription = null,
            )
        }
    }
}

@Composable
internal fun SearchBar(
    searchQuery: MutableState<String>?,
    navController: NavController,
    readOnly: Boolean,
    onSearch: (String) -> Unit = {},
    saveHistory: (String) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        readOnly = readOnly,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(48.dp)
            .onFocusChanged {
                if (it.isFocused && readOnly) {
                    navController.navigate(Screen.Search)
                }
            }
            .run {
                if (!readOnly) {
                    this.focusRequester(focusRequester)
                } else {
                    this
                }
            },
        value = searchQuery?.value ?: "",
        textStyle = MaterialTheme.typography.bodySmall,
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search),
                style = MaterialTheme.typography.bodySmall,
            )
        },
        leadingIcon = { Icon(painterResource(R.drawable.icon_search), contentDescription = null) },
        trailingIcon = {
            if (searchQuery?.value?.isEmpty() == false) {
                Icon(
                    painterResource(R.drawable.icon_close),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        searchQuery.value = ""
                        onSearch("")
                    },
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                saveHistory(searchQuery?.value ?: "")
            },
        ),
        onValueChange = { query ->
            if (query.length <= MAX_SEARCH_LENGTH) {
                searchQuery?.value = query
                onSearch(query)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
    )

    if (!readOnly) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CoinList(navController: NavController, coinPagingItems: LazyPagingItems<CoinResponse>) {
    val listState = rememberLazyListState()
    val isRefreshing = coinPagingItems.loadState.refresh is LoadState.Loading
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { coinPagingItems.refresh() },
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        state = pullToRefreshState,
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomEnd = 0.dp,
                bottomStart = 0.dp,
            ),
        ) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(coinPagingItems.itemCount) { index ->
                    coinPagingItems[index].let { coinItem ->
                        if (coinItem != null) {
                            CoinContent(navController, coinItem)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CoinContent(
    navController: NavController,
    coinItem: CoinResponse,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    Surface {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(colorScheme.surface)
                .clickable {
                    navController.navigate(Screen.Detail(coinId = coinItem.id))
                }
                .padding(vertical = 8.dp),
        ) {
            AsyncImage(
                model = coinItem.image,
                modifier = modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )

            Column(Modifier.padding(start = 16.dp)) {
                Row {
                    Text(
                        text = coinItem.name.take(10),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )

                    Text(
                        text = coinItem.symbol.formatSymbol(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = modifier.padding(top = 2.dp, start = 4.dp),
                    )
                }

                Text(
                    text = coinItem.totalVolume.formatVolume(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = modifier.padding(top = 4.dp),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = coinItem.currentPrice.formatPrice(coinItem.currencyCode),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(end = 8.dp),
            )

            val percentage = coinItem.priceChangePercentage24h
            val priceColor = when {
                percentage == null || percentage == 0.0 -> colorScheme.secondary
                percentage > 0 -> colorScheme.onPrimary
                else -> colorScheme.onTertiary
            }

            Text(
                textAlign = TextAlign.Center,
                text = coinItem.priceChangePercentage24h.formatPercentage(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = modifier
                    .drawBehind {
                        drawRoundRect(
                            cornerRadius = CornerRadius(10f, 10f),
                            color = priceColor,
                        )
                    }
                    .padding(6.dp)
                    .requiredWidth(60.dp),
            )
        }
    }
}
