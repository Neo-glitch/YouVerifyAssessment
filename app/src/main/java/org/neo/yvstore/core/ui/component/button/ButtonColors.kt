package org.neo.yvstore.core.ui.component.button

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.neo.yvstore.core.designSystem.util.DISABLED_CONTENT_ALPHA

val PrimaryButtonColors
    @Composable
    get() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(
            alpha = DISABLED_CONTENT_ALPHA,
        ),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(
            alpha = DISABLED_CONTENT_ALPHA,
        ),
    )

val SecondaryButtonColors
    @Composable
    get() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
            alpha = DISABLED_CONTENT_ALPHA,
        ),
        disabledContentColor = MaterialTheme.colorScheme.primary.copy(
            alpha = DISABLED_CONTENT_ALPHA,
        ),
    )

val WarningButtonColors
    @Composable
    get() = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError,
        disabledContainerColor = MaterialTheme.colorScheme.error.copy(
            alpha = DISABLED_CONTENT_ALPHA,
        ),
        disabledContentColor = MaterialTheme.colorScheme.onError.copy(
            alpha = DISABLED_CONTENT_ALPHA,
        ),
    )

val OutlinedButtonColors
    @Composable
    get() = ButtonDefaults.outlinedButtonColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = MaterialTheme.colorScheme.surface.copy(
            alpha = DISABLED_CONTENT_ALPHA,
        ),
        disabledContentColor = MaterialTheme.colorScheme.primary.copy(
            alpha = DISABLED_CONTENT_ALPHA,
        ),
    )

val DefaultButtonElevation
    @Composable
    get() = ButtonDefaults.buttonElevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        disabledElevation = 0.dp,
        focusedElevation = 0.dp,
        hoveredElevation = 0.dp,
    )
