package com.selim.cryptomarket.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.selim.cryptomarket.R
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.ui.navigation.Screen.Detail
import com.selim.cryptomarket.ui.navigation.Screen.Search
import com.selim.cryptomarket.ui.navigation.Screen.Settings
import com.selim.cryptomarket.util.formatPercentage
import com.selim.cryptomarket.util.formatPrice
import com.selim.cryptomarket.util.formatSymbol
import com.selim.cryptomarket.util.formatVolume

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
        content = {
            when (coins.loadState.refresh) {
                is LoadState.Loading -> {
                    LoadingState()
                }
                is LoadState.Error -> {
                    val error = coins.loadState.refresh as LoadState.Error
                    ErrorState(error.error.message.orEmpty())
                }
                else -> {
                    CoinList(navController, coins)
                }
            }
        }
    )
}

@Composable
private fun MainAppBar(navController: NavController, themeViewModel: ThemeViewModel, isDarkTheme: Boolean) {
    val colors = MaterialTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBar(searchQuery = null, navController, readOnly = true)

        val icon = if (isDarkTheme) R.drawable.icon_sun else R.drawable.icon_night

        IconButton(onClick = { themeViewModel.onChangeTheme() }) {
            Icon(
                painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(26.dp)
            )
        }

        IconButton(onClick = { navController.navigate(Settings.route) }) {
            Icon(
                painterResource(R.drawable.icon_settings),
                modifier = Modifier.size(26.dp),
                contentDescription = null
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
    saveHistory: (String) -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        readOnly = readOnly,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(48.dp)
            .onFocusChanged {
                if (it.isFocused && readOnly) {
                    navController.navigate(Search.route)
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
        textStyle = MaterialTheme.typography.caption,
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        placeholder = { Text(text = stringResource(id = R.string.search), style = MaterialTheme.typography.caption) },
        leadingIcon = { Icon(painterResource(R.drawable.icon_search), contentDescription = null) },
        trailingIcon = {
            if (searchQuery?.value?.isEmpty() == false) {
                Icon(painterResource(R.drawable.icon_close), contentDescription = null, modifier = Modifier.clickable {
                    searchQuery.value = ""
                    onSearch("")
                })
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                saveHistory(searchQuery?.value ?: "")
            }),
        onValueChange = { query ->
            searchQuery?.value = query
            onSearch(query)
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            backgroundColor = MaterialTheme.colors.secondaryVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )

    if (!readOnly) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
private fun CoinList(navController: NavController, coinPagingItems: LazyPagingItems<CoinResponse>) {
    val listState = rememberLazyListState()

    Card(
        backgroundColor = MaterialTheme.colors.onBackground,
        elevation = 8.dp,
        shape = RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        ),
        modifier = Modifier.padding(top = 8.dp)
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(coinPagingItems) { coinItem ->
                if (coinItem != null) {
                    CoinContent(navController, coinItem)
                }
            }
        }
    }
}

@Composable
private fun CoinContent(navController: NavController, coinItem: CoinResponse, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colors
    Surface {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(colors.onBackground)
                .clickable {
                    navController.navigate(
                        Detail.route.replace(
                            oldValue = "{id}",
                            newValue = coinItem.id
                        )
                    )
                }
                .padding(vertical = 8.dp)
        ) {
            AsyncImage(
                model = coinItem.image,
                modifier = modifier.size(44.dp),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )
            Column(Modifier.padding(start = 16.dp)) {
                Row {
                    Text(
                        text = coinItem.name,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = coinItem.symbol.formatSymbol(),
                        style = MaterialTheme.typography.caption,
                        modifier = modifier.padding(top = 2.dp, start = 4.dp)
                    )
                }
                Text(
                    text = coinItem.totalVolume.formatVolume(),
                    style = MaterialTheme.typography.caption
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = coinItem.currentPrice.formatPrice(coinItem.currencyCode),
                style = MaterialTheme.typography.subtitle1,
                modifier = modifier.padding(end = 8.dp)
            )

            val percentage = coinItem.priceChangePercentage24h
            val priceColor = when {
                percentage == null || percentage == 0.0 -> colors.secondary
                percentage > 0 -> Color(0xFF2FBE85)
                else -> Color(0xFFF6455D)
            }

            Text(
                textAlign = TextAlign.Center,
                text = coinItem.priceChangePercentage24h.formatPercentage(),
                style = MaterialTheme.typography.subtitle1,
                color = Color.White,
                modifier = modifier
                    .drawBehind {
                        drawRoundRect(
                            cornerRadius = CornerRadius(10f, 10f),
                            color = priceColor
                        )
                    }
                    .padding(6.dp)
                    .requiredWidth(60.dp)
            )
        }
    }
}

// TODO typography
// TODO renkler
// TODO search
// TODO row error
// TODO row loading
// TODO theme
// TODO nested scroll
// TODO review JJJ
// TODO nested scroll
// TODO refresh
// TODO splash
// TODO rank
// TODO app icon
// TODO placeholder
// TODO format pricing
// TODO bottomsheet bg