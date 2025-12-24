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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<SettingsViewModel>()
    val currencyPreference = viewModel.currencyCode.collectAsState(initial = "USD").value

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_back),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
        ) {
            SettingsItem(
                coroutineScope,
                sheetState,
                R.drawable.icon_dollar,
                R.string.currency,
                currencyPreference
            )

            HorizontalDivider(Modifier.padding(horizontal = 16.dp))

            SettingsItem(coroutineScope, sheetState, R.drawable.icon_text, R.string.language, "EN")
        }

        if (sheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = { coroutineScope.launch { sheetState.hide() } },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                BottomSheet(currencyPreference)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsItem(
    coroutineScope: CoroutineScope,
    sheetState: SheetState,
    iconResId: Int,
    textResId: Int,
    preference: String
) {
    Row(
        modifier = Modifier
            .clickable {
                coroutineScope.launch {
                    sheetState.show()
                }
            }
            .padding(16.dp),
        verticalAlignment = CenterVertically
    ) {
        Icon(
            modifier = Modifier.align(CenterVertically),
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = stringResource(id = textResId),
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = preference,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        Icon(
            modifier = Modifier.padding(start = 12.dp),
            painter = painterResource(id = R.drawable.icon_right_arrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun BottomSheet(currencyPreference: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrencyItem(R.string.usd, isSelected = currencyPreference == "USD")
        CurrencyItem(R.string.eur, isSelected = currencyPreference == "EUR")
        CurrencyItem(R.string.tl, isSelected = currencyPreference == "TRY")
    }
}

@Composable
fun CurrencyItem(textResId: Int, isSelected: Boolean) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current

    Row {
        val (textColor, fontWeight) = if (isSelected) {
            colorScheme.primary to FontWeight.Bold
        } else {
            colorScheme.tertiary to FontWeight.Normal
        }
        val text = stringResource(id = textResId)

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    settingsViewModel.onCurrencySelected(text)
                    context.getActivity()?.restart()
                }
                .padding(vertical = 8.dp),
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            fontWeight = fontWeight
        )
    }
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
