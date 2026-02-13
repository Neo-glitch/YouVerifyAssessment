package org.neo.yvstore.core.ui.component.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme

@Composable
fun YVStoreTopBar(
    title: String,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCenteredAligned: Boolean = true,
    actions: @Composable (RowScope.() -> Unit) = {},
) {
    if (isCenteredAligned) {
        CenteredTopBar(
            title = title,
            modifier = modifier,
            onNavigationClick = onNavigationClick,
            actions = actions,
        )
    } else {
        SimpleTopBar(
            title = title,
            modifier = modifier,
            onNavigationClick = onNavigationClick,
            actions = actions,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CenteredTopBar(
    title: String,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit) = {},
) {
    CenterAlignedTopAppBar(
        title = { TopBarTitle(title = title) },
        navigationIcon = {
            NavigationIcon(
                onNavigationClick = onNavigationClick,
            )
        },
        actions = actions,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleTopBar(
    title: String,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit) = {},
) {
    TopAppBar(
        title = {
            TopBarTitle(
                title = title,
            )
        },
        navigationIcon = {
            NavigationIcon(
                onNavigationClick = onNavigationClick,
            )
        },
        actions = actions,
        modifier = modifier
    )
}

@Composable
private fun NavigationIcon(onNavigationClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onNavigationClick,
        modifier = modifier,
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(R.drawable.ic_back_arrow),
            contentDescription = stringResource(R.string.app_bar_nav_icon_content_desc),
            tint = YVStoreTheme.colors.navigationColors.navigationIcon,
        )
    }
}

@Composable
private fun TopBarTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = title,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontSize = 24.sp,
            color = YVStoreTheme.colors.textColors.textToolbarTitle,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun YVStoreCenteredTopBarPreview() {
    YVStoreTheme {
        YVStoreTopBar(
            title = "YV Store",
            onNavigationClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreSimpleTopBarPreview() {
    YVStoreTheme {
        YVStoreTopBar(
            title = "YV Store",
            onNavigationClick = {},
            isCenteredAligned = false,
        )
    }
}
