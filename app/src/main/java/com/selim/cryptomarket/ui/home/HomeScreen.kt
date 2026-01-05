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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.selim.cryptomarket.R
import com.selim.cryptomarket.ui.navigation.Screen
import com.selim.cryptomarket.ui.search.SearchBar
import com.selim.cryptomarket.ui.theme.ThemeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel,
    isDarkTheme: Boolean,
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val coins = homeViewModel.coins.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            MainAppBar(
                isDarkTheme = isDarkTheme,
                onToggleTheme = { themeViewModel.onChangeTheme() },
                onSettingsClick = { navController.navigate(Screen.Settings) },
                onSearchClick = { navController.navigate(Screen.Search) },
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (coins.loadState.refresh) {
                    is LoadState.Loading -> LoadingState()
                    is LoadState.Error -> ErrorState()
                    else -> CoinList(
                        coins = coins,
                        onCoinClick = { coinId ->
                            navController.navigate(Screen.Detail(coinId = coinId))
                        },
                    )
                }
            }
        },
    )
}

@Composable
private fun MainAppBar(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onSettingsClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SearchBar(
            searchQuery = "",
            onSearchClick = onSearchClick,
            readOnly = true,
            onQueryChange = {},
        )

        val icon = if (isDarkTheme) R.drawable.icon_sun else R.drawable.icon_night

        IconButton(onClick = onToggleTheme) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Toggle Theme",
                modifier = Modifier.size(26.dp),
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }

        IconButton(onClick = onSettingsClick) {
            Icon(
                painter = painterResource(R.drawable.icon_settings),
                modifier = Modifier.size(26.dp),
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CoinList(
    coins: LazyPagingItems<CoinUiModel>,
    onCoinClick: (String) -> Unit,
) {
    val listState = rememberLazyListState()
    val isRefreshing = coins.loadState.refresh is LoadState.Loading
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { coins.refresh() },
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
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(
                    count = coins.itemCount,
                    key = { index -> coins.peek(index)?.id ?: "item_$index" },
                ) { index ->
                    val uiModel = coins[index]
                    if (uiModel != null) {
                        CoinContent(
                            uiModel = uiModel,
                            onClick = { onCoinClick(uiModel.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CoinContent(
    uiModel: CoinUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val priceColor = remember(uiModel.changeColorType) {
        when (uiModel.changeColorType) {
            ChangeColorType.POSITIVE -> colorScheme.onPrimary
            ChangeColorType.NEGATIVE -> colorScheme.onTertiary
            else -> colorScheme.secondary
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
    ) {
        AsyncImage(
            model = uiModel.imageUrl,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit,
            contentDescription = "${uiModel.name} icon",
        )

        Column(Modifier.padding(start = 16.dp)) {
            Row {
                Text(
                    text = uiModel.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                )

                Text(
                    text = uiModel.symbol,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp, start = 4.dp),
                )
            }

            Text(
                text = uiModel.volume,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = uiModel.price,
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(end = 8.dp),
        )

        Text(
            textAlign = TextAlign.Center,
            text = uiModel.changePercentage,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            modifier = Modifier
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
