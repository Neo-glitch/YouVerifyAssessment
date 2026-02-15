package org.neo.yvstore.core.ui.component.dialog

import androidx.compose.foundation.layout.Box
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
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.button.YVStoreWarningButton

@Composable
fun YVStoreWarningDialog(
    icon: Int,
    title: String,
    description: String,
    onDismiss: () -> Unit,
    onButtonClick: () -> Unit,
    buttonText: String,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = true),
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
        ) {
            WarningDialogContent(
                icon = icon,
                title = title,
                description = description,
                onDismiss = onDismiss,
                onPrimaryButtonClick = onButtonClick,
                primaryButtonText = buttonText,
            )
        }
    }
}

@Composable
private fun WarningDialogContent(
    icon: Int,
    title: String,
    description: String,
    onDismiss: () -> Unit,
    onPrimaryButtonClick: () -> Unit,
    primaryButtonText: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DialogIcon(icon = icon)
        Spacer(modifier = Modifier.height(16.dp))
        DialogTitle(title = title)
        Spacer(modifier = Modifier.height(8.dp))
        DefaultDialogDescription(description = description)
        Spacer(modifier = Modifier.height(24.dp))
        WarningDialogButton(
            primaryButtonText = primaryButtonText,
            onPrimaryButtonClick = onPrimaryButtonClick,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun WarningDialogButton(
    primaryButtonText: String,
    onPrimaryButtonClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    YVStoreWarningButton(
        modifier = Modifier.fillMaxWidth(),
        text = primaryButtonText,
        onClick = {
            onPrimaryButtonClick()
            onDismiss()
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun WarningDialogContentPreview() {
    YVStoreTheme {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            WarningDialogContent(
                icon = R.drawable.ic_error_alert,
                title = "Warning!",
                description = "This action cannot be undone. Please review before proceeding.",
                onDismiss = {},
                onPrimaryButtonClick = {},
                primaryButtonText = "I Understand",
            )
        }
    }
}
