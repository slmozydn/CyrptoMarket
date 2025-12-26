package com.selim.cryptomarket.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.selim.cryptomarket.ui.home.ErrorState
import com.selim.cryptomarket.ui.home.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(coinId: String) {
    val viewModel = hiltViewModel<DetailViewModel>()

    LaunchedEffect(coinId) {
        viewModel.getDetail(coinId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()

    when {
        uiState.loading && uiState.displayModel == null -> {
            LoadingState()
        }

        uiState.errorMessage != null -> {
            ErrorState(message = uiState.errorMessage!!)
        }

        else -> {
            val displayModel = uiState.displayModel
            if (displayModel != null) {
                DetailContent(
                    displayModel = displayModel,
                    timeRange = uiState.timeRange,
                    isRefreshing = uiState.isRefreshing,
                    pullToRefreshState = pullToRefreshState,
                    onRefresh = { viewModel.getDetail(coinId) },
                    onTimeRangeChanged = { range -> viewModel.onTimeRangeChange(coinId, range) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailContent(
    displayModel: CoinDisplayModel,
    timeRange: TimeRange,
    isRefreshing: Boolean,
    pullToRefreshState: PullToRefreshState,
    onRefresh: () -> Unit,
    onTimeRangeChanged: (TimeRange) -> Unit,
) {
    val currencyText = remember(displayModel.name, displayModel.symbol) {
        "${displayModel.name} (${displayModel.symbol})"
    }

    Surface {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize(),
            state = pullToRefreshState,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    PriceHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .padding(horizontal = 16.dp),
                        currency = currencyText,
                        icon = displayModel.iconUrl,
                        price = displayModel.currentPrice,
                        changeRate = displayModel.changePercentage,
                        isChangeRatePositive = displayModel.isChangePositive,
                    )
                }

                if (displayModel.chartEntries != null) {
                    item {
                        TimeRangePicker(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            selectedTimeRange = timeRange,
                            onTimeRangeSelected = onTimeRangeChanged,
                        )
                    }

                    item {
                        val context = LocalContext.current
                        val lineDataSet = remember(
                            displayModel.chartEntries,
                            displayModel.isChangePositive,
                        ) {
                            ChartUtils.createLineDataSet(
                                context = context,
                                isPositive = displayModel.isChangePositive,
                                chartValues = displayModel.chartEntries,
                            )
                        }

                        Chart(
                            modifier = Modifier.fillMaxWidth(),
                            lineDataSet = lineDataSet,
                        )
                    }
                }

                item {
                    Price(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        athPrice = displayModel.athPrice,
                        atlPrice = displayModel.atlPrice,
                        highPrice = displayModel.highestPrice,
                        lowPrice = displayModel.lowestPrice,
                        averagePrice = displayModel.volume,
                        changePrice = displayModel.changePrice,
                    )
                }

                item {
                    AboutChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        aboutChart = displayModel.description,
                    )
                }
            }
        }
    }
}
