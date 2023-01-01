package com.selim.cryptomarket.ui.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.selim.cryptomarket.R
import com.selim.cryptomarket.data.CoinResponse
import com.selim.cryptomarket.util.formatPercentage
import com.selim.cryptomarket.util.formatPrice
import com.selim.cryptomarket.util.formatSymbol
import com.selim.cryptomarket.util.formatVolume

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(themeViewModel: ThemeViewModel, isDarkTheme: Boolean) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val coins = homeViewModel.coins.collectAsLazyPagingItems()

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            Surface(modifier = Modifier.fillMaxWidth()) {
                Column {
                    MainAppBar(themeViewModel,isDarkTheme)
                }
            }
        },
        content = {
            when (coins.loadState.refresh) {
                is LoadState.Loading -> {
                    Loading()
                }
                is LoadState.Error -> {
                    val error = coins.loadState.refresh as LoadState.Error
                    ErrorColumn(error.error.message.orEmpty())
                }
                else -> {
                    CoinList(coins)
                }
            }
        }
    )
}

@Composable
private fun MainAppBar(themeViewModel: ThemeViewModel, isDarkTheme: Boolean) {
    val searchQuery = remember { mutableStateOf("") }
    val colors = MaterialTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SearchBar(searchQuery)

        val icon = if (isDarkTheme) R.drawable.icon_sun else R.drawable.icon_night

        IconButton(onClick = { themeViewModel.onChangeTheme() }) {
            Icon(
                painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(26.dp)
            )
        }

        IconButton(onClick = { }) {
            Icon(
                painterResource(R.drawable.icon_settings),
                modifier = Modifier.size(26.dp),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun SearchBar(searchQuery: MutableState<String>) {
    TextField(
        modifier = Modifier.height(48.dp),
        value = searchQuery.value,
        textStyle = MaterialTheme.typography.caption,
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        placeholder = { Text(text = "Search", style = MaterialTheme.typography.caption) },
        leadingIcon = { Icon(painterResource(R.drawable.icon_search), contentDescription = null) },
        trailingIcon = {
            AnimatedVisibility(visible = searchQuery.value.isNotEmpty()) {
                Icon(
                    painterResource(R.drawable.icon_close),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        searchQuery.value = ""
                        // onSearch("")
                    }
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onValueChange = { query ->
            searchQuery.value = query
            // onSearch(query)
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
}

@Composable
private fun CoinList(coinPagingItems: LazyPagingItems<CoinResponse>) {
    val listState = rememberLazyListState()

    Card(
        backgroundColor = MaterialTheme.colors.onBackground,
        elevation = 1.dp,
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
                    CoinContent(coinItem)
                }
            }
        }
    }
}

@Composable
private fun CoinContent(coinItem: CoinResponse, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colors
    Surface {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(colors.onBackground)
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
                    style = MaterialTheme.typography.caption,
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = coinItem.currentPrice.formatPrice(coinItem.currencyCode),
                style = MaterialTheme.typography.subtitle1,
                modifier = modifier.padding(end = 8.dp),
            )

            val percentage = coinItem.priceChangePercentage24h
            val color = when {
                percentage == null || percentage == 0.0 -> colors.secondary
                percentage > 0 -> colors.onPrimary
                else -> colors.onSecondary
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
                            color = color
                        )
                    }
                    .padding(6.dp)
                    .requiredWidth(62.dp)
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