package org.neo.yvstore.core.ui.component.input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme

@Composable
fun YVStoreSearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    autoFocus: Boolean = false,
    debounceDelay: Long = 0L,
    maxLines: Int = 1,
    onSearch: () -> Unit = {},
    onFocusChange: ((Boolean) -> Unit)? = null,
) {
    val currentOnSearch by rememberUpdatedState(onSearch)
    val currentValue by rememberUpdatedState(value)

    LaunchedEffect(Unit) {
        snapshotFlow { currentValue }
            .drop(1)
            .collectLatest {
                if (debounceDelay > 0) {
                    delay(debounceDelay)
                }
                currentOnSearch()
            }
    }

    YVStoreTextInput(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search",
                tint = YVStoreTheme.colors.iconColors.inputPrefixIcon,
            )
        },
        placeholder = placeholder,
        imeAction = ImeAction.Search,
        onDoneClick = onSearch,
        enabled = enabled,
        autoFocus = autoFocus,
        maxLines = maxLines,
        onFocusChange = onFocusChange,
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Words,
    )
}

@Preview(showBackground = true)
@Composable
private fun YVStoreSearchInputPreview() {
    YVStoreTheme {
        Box(
            modifier = Modifier.padding(16.dp),
        ) {
            var searchQuery by remember { mutableStateOf("") }
            YVStoreSearchInput(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                onSearch = {},
                placeholder = "Search products...",
            )
        }
    }
}
