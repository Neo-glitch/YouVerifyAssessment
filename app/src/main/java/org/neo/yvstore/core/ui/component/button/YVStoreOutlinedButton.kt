package org.neo.yvstore.core.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.designSystem.util.DISABLED_CONTENT_ALPHA
import org.neo.yvstore.core.ui.component.progress.YVStoreCircleProgressIndicator

@Composable
fun YVStorePrimaryOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        fontSize = 18.sp,
    ),
    shape: Shape = RoundedCornerShape(8.dp),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 12.dp,
    ),
    buttonHeight: Dp = 56.dp,
) {
    val borderColor = if (enabled) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = DISABLED_CONTENT_ALPHA)
    }

    val loadingContentColor = if (enabled) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = DISABLED_CONTENT_ALPHA)
    }

    OutlinedButton(
        modifier = modifier.height(buttonHeight),
        onClick = if (!loading) onClick else ({}),
        enabled = enabled,
        contentPadding = contentPadding,
        colors = OutlinedButtonColors,
        elevation = DefaultButtonElevation,
        shape = shape,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor,
        ),
    ) {
        if (loading) {
            YVStoreCircleProgressIndicator(
                size = 24.dp,
                color = loadingContentColor,
            )
        } else {
            Text(
                text = text,
                style = textStyle,
                modifier = Modifier,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStorePrimaryOutlinedButtonEnabledPreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStorePrimaryOutlinedButton(
                text = "Outlined Button",
                onClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStorePrimaryOutlinedButtonDisabledPreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStorePrimaryOutlinedButton(
                text = "Outlined Button",
                onClick = {},
                enabled = false,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStorePrimaryOutlinedButtonLoadingPreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStorePrimaryOutlinedButton(
                text = "Outlined Button",
                onClick = {},
                loading = true,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStorePrimaryOutlinedButtonPillShapePreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStorePrimaryOutlinedButton(
                text = "View details",
                onClick = {},
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 8.dp,
                ),
                buttonHeight = 40.dp,
                textStyle = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

