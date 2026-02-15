package org.neo.yvstore.core.ui.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.designSystem.util.DISABLED_CONTENT_ALPHA
import org.neo.yvstore.core.ui.component.progress.YVStoreCircleProgressIndicator

@Composable
fun YVStorePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium.copy(
        fontSize = 18.sp,
    ),
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 16.dp,
    ),
    buttonHeight: Dp = 56.dp,
) {
    val loadingContentColor =
        if (enabled) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onPrimary.copy(
                alpha = DISABLED_CONTENT_ALPHA,
            )
        }

    Button(
        modifier = modifier
            .then(
                Modifier.height(buttonHeight),
            ),
        onClick = if (!loading) onClick else ({}),
        enabled = enabled,
        contentPadding = contentPadding,
        elevation = DefaultButtonElevation,
        colors = PrimaryButtonColors,
        shape = shape,
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
            )
        }
    }
}

@Composable
fun YVStoreSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium.copy(
        fontSize = 18.sp,
    ),
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 16.dp,
    ),
    buttonHeight: Dp = 56.dp,
) {
    val loadingContentColor =
        if (enabled) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.primary.copy(
                alpha = DISABLED_CONTENT_ALPHA,
            )
        }

    Button(
        modifier = modifier
            .then(
                Modifier.height(buttonHeight),
            ),
        onClick = if (!loading) onClick else ({}),
        enabled = enabled,
        contentPadding = contentPadding,
        elevation = DefaultButtonElevation,
        colors = SecondaryButtonColors,
        shape = shape,
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
            )
        }
    }
}

@Composable
fun YVStorePrimaryIconButton(
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    shape: Shape = RoundedCornerShape(8.dp),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 16.dp,
    ),
) {
    val loadingContentColor =
        if (enabled) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onPrimary.copy(
                alpha = DISABLED_CONTENT_ALPHA,
            )
        }

    Button(
        modifier = modifier,
        onClick = if (!loading) onClick else ({}),
        enabled = enabled,
        contentPadding = contentPadding,
        elevation = DefaultButtonElevation,
        colors = PrimaryButtonColors,
        shape = shape,
    ) {
        if (loading) {
            YVStoreCircleProgressIndicator(
                size = 24.dp,
                color = loadingContentColor,
            )
        } else {
            Icon(
                painter = painterResource(icon),
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
fun YVStoreSecondaryIconButton(
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    shape: Shape = RoundedCornerShape(8.dp),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 16.dp,
    ),
) {
    val loadingContentColor =
        if (enabled) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.primary.copy(
                alpha = DISABLED_CONTENT_ALPHA,
            )
        }

    Button(
        modifier = modifier,
        onClick = if (!loading) onClick else ({}),
        enabled = enabled,
        contentPadding = contentPadding,
        elevation = DefaultButtonElevation,
        colors = SecondaryButtonColors,
        shape = shape,
    ) {
        if (loading) {
            YVStoreCircleProgressIndicator(
                size = 24.dp,
                color = loadingContentColor,
            )
        } else {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(icon),
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun YVStoreWarningButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        fontSize = 18.sp,
    ),
    shape: Shape = RoundedCornerShape(12.dp),
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 16.dp,
    ),
    buttonHeight: Dp = 56.dp,
) {
    val loadingContentColor =
        if (enabled) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.error.copy(
                alpha = DISABLED_CONTENT_ALPHA,
            )
        }

    Button(
        modifier = modifier
            .then(
                Modifier.height(buttonHeight),
            ),
        onClick = if (!loading) onClick else ({}),
        enabled = enabled,
        contentPadding = contentPadding,
        elevation = DefaultButtonElevation,
        colors = WarningButtonColors,
        shape = shape,
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
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStorePrimaryButtonEnabledPreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStorePrimaryButton(
                text = "Primary Button",
                onClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStorePrimaryButtonDisabledPreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStorePrimaryButton(
                text = "Primary Button",
                onClick = {},
                enabled = false,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStorePrimaryButtonLoadingPreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStorePrimaryButton(
                text = "Primary Button",
                onClick = {},
                loading = true,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreSecondaryButtonEnabledPreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStoreSecondaryButton(
                text = "Secondary Button",
                onClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreSecondaryButtonDisabledPreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStoreSecondaryButton(
                text = "Secondary Button",
                onClick = {},
                enabled = false,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreSecondaryButtonLoadingPreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStoreSecondaryButton(
                text = "Secondary Button",
                onClick = {},
                loading = true,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStorePrimaryIconButtonEnabledPreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStorePrimaryIconButton(
                onClick = {},
                icon = R.drawable.ic_add,
                contentDescription = "Add",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreSecondaryIconButtonEnabledPreview() {
    YVStoreTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            YVStoreSecondaryIconButton(
                onClick = {},
                icon = R.drawable.ic_add,
                contentDescription = "Add",
            )
        }
    }
}

@Composable
fun YVStoreTextButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    enabled: Boolean = true,
) {
    Text(
        modifier = modifier.clickable(
            enabled = enabled,
            onClick = onClick,
        ),
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun YVStoreTextButtonPreview() {
    YVStoreTheme {
        YVStoreTextButton(
            onClick = {},
            text = "Test",
        )
    }
}

