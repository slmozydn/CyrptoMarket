package com.selim.cryptomarket.ui.detail

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.LaunchedEffect
import com.selim.cryptomarket.R
import com.selim.cryptomarket.ui.detail.DetailViewModel.DetailUiState
import com.selim.cryptomarket.ui.home.ErrorState
import com.selim.cryptomarket.ui.home.LoadingState
import com.selim.cryptomarket.ui.settings.CurrencyType.TRY
import com.selim.cryptomarket.ui.settings.CurrencyType.USD
import com.selim.cryptomarket.util.formatPercentage
import com.selim.cryptomarket.util.formatPrice
import com.selim.cryptomarket.util.formatVolume

@Composable
fun DetailScreen(coinId: String) {
    val viewModel = hiltViewModel<DetailViewModel>()

    LaunchedEffect(coinId) {
        viewModel.getDetail(coinId)
    }

    val uiState = viewModel.uiState.collectAsState().value

    when {
        uiState.loading && uiState.coinDetail == null -> LoadingState()
        uiState.error != null -> ErrorState(message = uiState.error.message.orEmpty())
        else -> DetailContent(viewModel, uiState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailContent(viewModel: DetailViewModel, uiState: DetailUiState) {
    val currencyCode = uiState.currencyCode
    val coin = uiState.coinDetail ?: return
    val currentPrice = when (currencyCode) {
        USD.value -> coin.marketData.currentPrice.usd
        TRY.value -> coin.marketData.currentPrice.tryX
        else -> coin.marketData.currentPrice.eur
    }.formatPrice(currencyCode)

    val highestPrice = when (currencyCode) {
        USD.value -> coin.marketData.high24h.usd
        TRY.value -> coin.marketData.high24h.tryX
        else -> coin.marketData.high24h.eur
    }.formatPrice(currencyCode)

    val lowestPrice = when (currencyCode) {
        USD.value -> coin.marketData.low24h.usd
        TRY.value -> coin.marketData.low24h.tryX
        else -> coin.marketData.low24h.eur
    }.formatPrice(currencyCode)

    val changePrice = when (currencyCode) {
        USD.value -> coin.marketData.priceChange24hInCurrency.usd
        TRY.value -> coin.marketData.priceChange24hInCurrency.tryX
        else -> coin.marketData.priceChange24hInCurrency.eur
    }.formatPrice(currencyCode)

    val changePercentage = coin.marketData.priceChangePercentage24h.formatPercentage()

    val athPrice = when (currencyCode) {
        USD.value -> coin.marketData.ath.usd
        TRY.value -> coin.marketData.ath.tryX
        else -> coin.marketData.ath.eur
    }.formatPrice()

    val atlPrice = when (currencyCode) {
        USD.value -> coin.marketData.atl.usd
        TRY.value -> coin.marketData.atl.tryX
        else -> coin.marketData.atl.eur
    }.formatPrice()

    val volume = when (currencyCode) {
        USD.value -> coin.marketData.totalVolume.usd
        TRY.value -> coin.marketData.totalVolume.tryX
        else -> coin.marketData.totalVolume.eur
    }.formatVolume().replace("Volume ", "")

    val isPositive = coin.marketData.priceChangePercentage24h > 0
    val chartValues = uiState.coinChart!!.prices.map {
        Entry(
            it[0].toFloat(),
            it[1].toFloat(),
        )
    }
    val pullToRefreshState = rememberPullToRefreshState()

    Surface {
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.getDetail(coin.id) },
            modifier = Modifier.fillMaxSize(),
            state = pullToRefreshState,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                item {
                    PriceHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        currency = coin.name + " (${coin.symbol.uppercase()})",
                        icon = coin.image.small,
                        price = currentPrice,
                        changeRate = changePercentage,
                        isChangeRatePositive = isPositive,
                    )
                }

                item {
                    TimeRangePicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        selectedTimeRange = uiState.timeRange,
                    ) { timeRange ->
                        viewModel.onTimeRangeChange(coin.id, timeRange)
                    }
                }

                item {
                    Chart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        lineDataSet = getLineDataSet(LocalContext.current, isPositive, chartValues),
                    )
                }

                item {
                    Price(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        athPrice = athPrice,
                        atlPrice = atlPrice,
                        highPrice = highestPrice,
                        lowPrice = lowestPrice,
                        averagePrice = volume,
                        changePrice = changePrice,
                    )
                }

                item {
                    AboutChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                        aboutChart = coin.description.en,
                    )
                }
            }
        }
    }
}

fun getLineDataSet(context: Context, isPositive: Boolean, chartValues: List<Entry>) =
    LineDataSet(chartValues, "market_price").apply {
        val (colorX, highLightColorX) = if (isPositive) {
            R.color.greenish_teal to R.color.black
        } else {
            R.color.watermelon to R.color.black
        }
        mode = LineDataSet.Mode.CUBIC_BEZIER
        color = ContextCompat.getColor(context, colorX)
        highLightColor = ContextCompat.getColor(context, highLightColorX)
        fillDrawable = if (isPositive) {
            ContextCompat.getDrawable(context, R.drawable.background_positive_chart)
        } else {
            ContextCompat.getDrawable(context, R.drawable.background_negative_chart)
        }
        lineWidth = 1f
        setDrawFilled(true)
        setDrawCircles(false)
    }
