package org.neo.yvstore.core.ui.component.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme

@Composable
fun YVStoreLoadingContent(
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.loading_label),
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        YVStoreCircleProgressIndicator(size = 48.dp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = YVStoreTheme.colors.textColors.textPrimary,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreLoadingContentPreview() {
    YVStoreTheme {
        YVStoreLoadingContent()
    }
}
