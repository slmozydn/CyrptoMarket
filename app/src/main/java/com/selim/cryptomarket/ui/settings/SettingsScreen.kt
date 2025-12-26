package com.selim.cryptomarket.ui.settings

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.selim.cryptomarket.R
import com.selim.cryptomarket.util.restart
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val currencyPreference by viewModel.currencyCode.collectAsState(initial = "USD")
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var showBottomSheet by remember { mutableStateOf(false) }

    BackHandler(showBottomSheet) {
        coroutineScope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            showBottomSheet = false
        }
    }

    SettingsContent(
        navController = navController,
        currencyPreference = currencyPreference,
        sheetState = sheetState,
        showBottomSheet = showBottomSheet,
        onSettingClick = {
            showBottomSheet = true
        },
        onDismissSheet = {
            coroutineScope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                showBottomSheet = false
            }
        },
        onCurrencySelected = { selectedCurrency ->
            viewModel.onCurrencySelected(selectedCurrency)

            coroutineScope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                showBottomSheet = false
                context.getActivity()?.restart()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    navController: NavController,
    currencyPreference: String,
    sheetState: SheetState,
    showBottomSheet: Boolean,
    onSettingClick: () -> Unit,
    onDismissSheet: () -> Unit,
    onCurrencySelected: (String) -> Unit
) {
    Scaffold(
        topBar = {
            SettingsTopBar(onBackClick = { navController.navigateUp() })
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(paddingValues),
        ) {
            SettingsItem(
                iconResId = R.drawable.icon_dollar,
                textResId = R.string.currency,
                value = currencyPreference,
                onClick = onSettingClick
            )

            HorizontalDivider(Modifier.padding(horizontal = 16.dp))

            SettingsItem(
                iconResId = R.drawable.icon_text,
                textResId = R.string.language,
                value = "EN",
                onClick = onSettingClick
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = onDismissSheet,
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                CurrencyBottomSheetContent(
                    selectedCurrency = currencyPreference,
                    onCurrencySelected = onCurrencySelected
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopBar(onBackClick: () -> Unit) {
    Surface(
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.settings_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_back),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
        )
    }
}

@Composable
fun SettingsItem(
    iconResId: Int,
    textResId: Int,
    value: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = stringResource(id = textResId),
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )

        Icon(
            modifier = Modifier.padding(start = 12.dp),
            painter = painterResource(id = R.drawable.icon_right_arrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun CurrencyBottomSheetContent(
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val currencies = listOf(
            "USD" to R.string.usd,
            "EUR" to R.string.eur,
            "TRY" to R.string.tl
        )

        currencies.forEach { (code, nameRes) ->
            CurrencyItem(
                textResId = nameRes,
                currencyCode = code,
                isSelected = selectedCurrency == code,
                onCurrencySelected = onCurrencySelected
            )
        }
    }
}

@Composable
fun CurrencyItem(
    textResId: Int,
    currencyCode: String,
    isSelected: Boolean,
    onCurrencySelected: (String) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val textColor = if (isSelected) colorScheme.primary else colorScheme.tertiary
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCurrencySelected(currencyCode) }
            .padding(vertical = 12.dp),
        text = stringResource(id = textResId),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
        color = textColor,
        fontWeight = fontWeight,
    )
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
