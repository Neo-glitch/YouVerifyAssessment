package org.neo.yvstore.core.ui.extension

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role

@Composable
fun Modifier.clearFocusClickable(
    interactionSource: MutableInteractionSource? = remember { MutableInteractionSource() },
    indication: Indication? = LocalIndication.current,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier {
    val focusManager = LocalFocusManager.current

    return this.clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = {
            focusManager.clearFocus()
            onClick()
        },
    )
}

fun Modifier.noRippleClearFocusClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = composed {
    val focusManager = LocalFocusManager.current

    this.clickable(
        enabled = enabled,
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = {
            focusManager.clearFocus()
            onClick()
        },
    )
}
