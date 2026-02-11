package org.neo.yvstore.core.ui.component.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme

@Composable
fun YVStoreTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    enabled: Boolean = true,
    maxLength: Int? = null,
    showMaxLengthCounter: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    error: String? = null,
    label: String? = null,
    showError: Boolean = false,
    textFieldColors: TextFieldColors = YVStoreOutlinedTextInputColors,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = 16.sp,
        color = YVStoreTheme.colors.textColors.textPrimary,
    ),
    singleLine: Boolean = true,
    imeAction: ImeAction = ImeAction.Default,
    onDoneClick: () -> Unit = {},
    onFocusChange: ((Boolean) -> Unit)? = null,
    autoFocus: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(12.dp),
) {
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        label?.let {
            YVStoreInputLabel(
                text = label,
            )
        }

        YVStoreBasicTextInputField(
            value = value,
            onValueChange = onValueChange,
            maxLength = maxLength,
            focusRequester = focusRequester,
            interactionSource = interactionSource,
            onFocusChange = onFocusChange,
            enabled = enabled,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            textStyle = textStyle,
            keyboardType = keyboardType,
            capitalization = capitalization,
            imeAction = imeAction,
            keyboardController = keyboardController,
            onDoneClick = onDoneClick,
            placeholder = placeholder,
            prefix = prefix,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            showError = showError,
            textFieldColors = textFieldColors,
            shape = shape,
        )

        YVStoreInputStatusRow(
            modifier = Modifier.fillMaxWidth(),
            showError = showError,
            error = error,
            maxLength = maxLength,
            showMaxLengthCounter = showMaxLengthCounter,
            currentValue = value,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YVStoreTextInputPreview() {
    YVStoreTheme {
        YVStoreTextInput(
            modifier = Modifier.padding(40.dp),
            value = "",
            onValueChange = {},
            placeholder = "Placeholder",
            label = "Label",
        )
    }
}
