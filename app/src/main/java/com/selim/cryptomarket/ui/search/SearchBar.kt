package com.selim.cryptomarket.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.selim.cryptomarket.R

private const val MAX_SEARCH_LENGTH = 20

@Composable
internal fun SearchBar(
    searchQuery: String,
    readOnly: Boolean,
    onSearchClick: () -> Unit,
    onQueryChange: (String) -> Unit,
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
                    onSearchClick.invoke()
                }
            }
            .run {
                if (!readOnly) {
                    this.focusRequester(focusRequester)
                } else {
                    this
                }
            },
        value = searchQuery,
        textStyle = MaterialTheme.typography.bodySmall,
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search),
                style = MaterialTheme.typography.bodySmall,
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.icon_search),
                contentDescription = null,
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.icon_close),
                    contentDescription = "Clear",
                    modifier = Modifier.clickable {
                        onQueryChange("")
                    },
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                saveHistory(searchQuery)
            },
        ),
        onValueChange = { query ->
            if (query.length <= MAX_SEARCH_LENGTH) {
                onQueryChange(query)
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
