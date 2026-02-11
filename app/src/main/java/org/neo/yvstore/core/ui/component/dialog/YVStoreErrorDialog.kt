package org.neo.yvstore.core.ui.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.button.YVStorePrimaryButton
import org.neo.yvstore.core.ui.component.button.YVStoreSecondaryButton

@Composable
fun YVStoreErrorDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    onPrimaryButtonClick: () -> Unit,
    primaryButtonText: String,
    onSecondaryButtonClick: (() -> Unit)? = null,
    secondaryButtonText: String? = null,
    properties: DialogProperties = DialogProperties(
        dismissOnClickOutside = false,
        dismissOnBackPress = false,
    ),
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = properties,
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
        ) {
            DialogContent(
                title = title,
                description = description,
                onDismiss = onDismiss,
                onPrimaryButtonClick = onPrimaryButtonClick,
                onSecondaryButtonClick = onSecondaryButtonClick,
                primaryButtonText = primaryButtonText,
                secondaryButtonText = secondaryButtonText,
            )
        }
    }
}

@Composable
private fun DialogContent(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    onPrimaryButtonClick: () -> Unit,
    onSecondaryButtonClick: (() -> Unit)?,
    primaryButtonText: String,
    secondaryButtonText: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DialogTitle(title = title)
        Spacer(modifier = Modifier.height(8.dp))
        DefaultDialogDescription(description)
        Spacer(modifier = Modifier.height(24.dp))
        DialogActionButtons(
            primaryButtonText = primaryButtonText,
            secondaryButtonText = secondaryButtonText,
            onPrimaryButtonClick = onPrimaryButtonClick,
            onSecondaryButtonClick = onSecondaryButtonClick,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun DialogActionButtons(
    primaryButtonText: String,
    secondaryButtonText: String?,
    onPrimaryButtonClick: () -> Unit,
    onSecondaryButtonClick: (() -> Unit)?,
    onDismiss: () -> Unit,
) {
    val hasSingleButton = onSecondaryButtonClick == null

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (hasSingleButton) {
            YVStoreSecondaryButton(
                text = primaryButtonText,
                onClick = {
                    onPrimaryButtonClick()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            YVStorePrimaryButton(
                text = primaryButtonText,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onPrimaryButtonClick()
                    onDismiss()
                },
            )

            Spacer(modifier = Modifier.height(12.dp))

            secondaryButtonText?.let {
                YVStoreSecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = it,
                    onClick = {
                        onSecondaryButtonClick()
                        onDismiss()
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogContentPreview() {
    YVStoreTheme {
        DialogContent(
            title = "Error!",
            description = "Oops something went wrong.",
            onDismiss = {},
            onPrimaryButtonClick = {},
            onSecondaryButtonClick = {},
            primaryButtonText = "Continue",
            secondaryButtonText = "Go Back",
        )
    }
}
