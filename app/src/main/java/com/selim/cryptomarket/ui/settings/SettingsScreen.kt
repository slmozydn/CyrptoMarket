package com.selim.cryptomarket.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.selim.cryptomarket.R
import com.selim.cryptomarket.ui.settings.SettingsViewModel
import com.selim.cryptomarket.util.restart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 8.dp,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_back),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                            .align(CenterVertically)
                            .padding(end = 16.dp)
                            .clickable { navController.navigateUp() },
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.settings_title),
                        style = MaterialTheme.typography.h2,
                    )
                }
            }
        }
    ) {
        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
        )
        val coroutineScope = rememberCoroutineScope()
        val viewModel = hiltViewModel<SettingsViewModel>()
        val currencyPreference = viewModel.currencyCode.collectAsState(initial = "USD").value

        BackHandler(sheetState.isVisible) {
            coroutineScope.launch { sheetState.hide() }
        }

        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = { BottomSheet(currencyPreference) },
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            scrimColor = Color.Black.copy(alpha = 0.6f),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {
            Column {
                SettingsItem(coroutineScope, sheetState, R.drawable.icon_dollar, R.string.currency, currencyPreference)
                Divider(Modifier.padding(horizontal = 16.dp))
                SettingsItem(coroutineScope, sheetState, R.drawable.icon_text, R.string.language, "EN")
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    text = "Version: 2.1.1",
                    style = MaterialTheme.typography.caption,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsItem(
    coroutineScope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    iconResId: Int,
    textResId: Int,
    preference: String
) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
            .clickable {
                coroutineScope.launch {
                    sheetState.show()
                }
            }
            .padding(16.dp)
    ) {
        Icon(
            modifier = Modifier.align(CenterVertically),
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = MaterialTheme.colors.onSecondary
        )
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = stringResource(id = textResId),
            style = MaterialTheme.typography.subtitle1,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = preference,
            style = MaterialTheme.typography.caption,
            fontSize = 14.sp
        )
        Icon(
            modifier = Modifier.padding(start = 12.dp),
            painter = painterResource(id = R.drawable.icon_right_arrow),
            contentDescription = null,
            tint = MaterialTheme.colors.primaryVariant
        )
    }
}

@Composable
fun BottomSheet(currencyPreference: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.onBackground)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = painterResource(id = R.drawable.icon_bottom_sheet), contentDescription = null)
        CurrencyItem(R.string.usd, isSelected = currencyPreference == "USD")
        CurrencyItem(R.string.eur, isSelected = currencyPreference == "EUR")
        CurrencyItem(R.string.tl, isSelected = currencyPreference == "TRY")
    }
}

@Composable
fun CurrencyItem(textResId: Int, isSelected: Boolean) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val colors = MaterialTheme.colors
    val context = LocalContext.current
    Row {
        val (textColor, fontWeight) = if (isSelected) {
            colors.surface to FontWeight.Bold
        } else {
            colors.primaryVariant to FontWeight.Normal
        }
        val text = stringResource(id = textResId)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    settingsViewModel.onCurrencySelected(text)
                    context
                        .getActivity()
                        ?.restart()
                }
                .padding(vertical = 8.dp),
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1,
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
