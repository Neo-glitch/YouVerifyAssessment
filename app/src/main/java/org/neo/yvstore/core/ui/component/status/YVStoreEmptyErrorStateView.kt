package org.neo.yvstore.core.ui.component.status

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.button.YVStorePrimaryButton

@Composable
fun YVStoreEmptyErrorStateView(
    @DrawableRes image: Int,
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    actionButtonText: String? = null,
    onActionButtonClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = stringResource(R.string.empty_error_state_image_content_desc),
            modifier = Modifier.size(200.dp),
        )

        title?.let {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = YVStoreTheme.colors.textColors.textEmptyErrorStateTitle,
                ),
                textAlign = TextAlign.Center,
            )
        }

        description?.let {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = YVStoreTheme.colors.textColors.textSecondary,
            )
        }

        actionButtonText?.let {
            Spacer(modifier = Modifier.height(56.dp))

            YVStorePrimaryButton(
                text = it,
                onClick = onActionButtonClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreEmptyErrorStateViewWithButtonPreview() {
    YVStoreTheme {
        YVStoreEmptyErrorStateView(
            image = android.R.drawable.ic_dialog_info,
            title = "No Results Found",
            description = "We couldn't find any products matching your search.",
            actionButtonText = "Add New Product",
            onActionButtonClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreEmptyErrorStateViewWithoutButtonPreview() {
    YVStoreTheme {
        YVStoreEmptyErrorStateView(
            image = android.R.drawable.ic_dialog_info,
            title = "No Data Available",
            description = "There is no data to display at this time. Please check back later.",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreEmptyErrorStateViewTitleOnlyPreview() {
    YVStoreTheme {
        YVStoreEmptyErrorStateView(
            image = android.R.drawable.ic_dialog_info,
            title = "Empty State",
        )
    }
}
