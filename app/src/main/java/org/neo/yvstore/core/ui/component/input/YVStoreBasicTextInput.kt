package org.neo.yvstore.core.ui.component.input

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.designSystem.util.DISABLED_CONTENT_ALPHA
import org.neo.yvstore.core.ui.util.INPUT_ERROR_TAG
import org.neo.yvstore.core.ui.util.INPUT_LABEL_TAG
import org.neo.yvstore.core.ui.util.INPUT_PLACE_HOLDER_TAG
import org.neo.yvstore.core.ui.util.INPUT_SENSITIVE_BUTTON_TAG

val YVStoreOutlinedTextInputColors: TextFieldColors
    @Composable
    get() = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        errorBorderColor = MaterialTheme.colorScheme.error,
        disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = DISABLED_CONTENT_ALPHA),
        focusedPlaceholderColor = YVStoreTheme.colors.textColors.textTertiary,
        unfocusedPlaceholderColor = YVStoreTheme.colors.textColors.textTertiary,
        disabledPlaceholderColor = YVStoreTheme.colors.textColors.textTertiary.copy(alpha = DISABLED_CONTENT_ALPHA),
        focusedTextColor = YVStoreTheme.colors.textColors.textPrimary,
        unfocusedTextColor = YVStoreTheme.colors.textColors.textPrimary,
        disabledTextColor = YVStoreTheme.colors.textColors.textPrimary.copy(alpha = DISABLED_CONTENT_ALPHA),
        errorTextColor = MaterialTheme.colorScheme.error,
        cursorColor = MaterialTheme.colorScheme.onSurface,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = DISABLED_CONTENT_ALPHA),
        focusedSuffixColor = YVStoreTheme.colors.iconColors.inputSuffixIcon,
        unfocusedSuffixColor = YVStoreTheme.colors.iconColors.inputSuffixIcon,
        disabledSuffixColor = YVStoreTheme.colors.iconColors.inputSuffixIcon.copy(alpha = DISABLED_CONTENT_ALPHA),
        errorSuffixColor = MaterialTheme.colorScheme.error,
        focusedPrefixColor = YVStoreTheme.colors.iconColors.inputPrefixIcon,
        disabledPrefixColor = YVStoreTheme.colors.iconColors.inputPrefixIcon.copy(alpha = DISABLED_CONTENT_ALPHA),
        unfocusedPrefixColor = YVStoreTheme.colors.iconColors.inputPrefixIcon,
    )

@Composable
fun YVStoreInputPlaceholder(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.testTag(INPUT_PLACE_HOLDER_TAG),
        style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 16.sp,
            color = YVStoreTheme.colors.textColors.textTertiary,
        ),
    )
}

@Composable
fun YVStoreInputLabel(text: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(bottom = 8.dp)) {
        Text(
            text = text,
            modifier = Modifier.testTag(INPUT_LABEL_TAG),
            style = MaterialTheme.typography.   bodySmall.copy(
                fontSize = 14.sp,
                color = YVStoreTheme.colors.textColors.textPrimary,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Composable
fun YVStoreInputSensitiveIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colorFilter: ColorFilter? = null,
    showSensitiveInfo: Boolean = false,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.testTag(INPUT_SENSITIVE_BUTTON_TAG),
    ) {
        val drawableRes =
            if (showSensitiveInfo) {
                R.drawable.ic_eye_open
            } else {
                R.drawable.ic_eye_closed
            }


        Image(
            imageVector = ImageVector.vectorResource(drawableRes),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
            contentDescription = if (showSensitiveInfo) "Hide password" else "Show password",
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
fun YVStoreInputError(text: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(top = 8.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.testTag(INPUT_ERROR_TAG),
        )
    }
}

@Composable
fun YVStoreBasicTextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    focusRequester: FocusRequester = remember { FocusRequester() },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onFocusChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    imeAction: ImeAction = ImeAction.Default,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    onDoneClick: () -> Unit = {},
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    showError: Boolean = false,
    textFieldColors: TextFieldColors = YVStoreOutlinedTextInputColors,
    shape: Shape = RoundedCornerShape(12.dp),
) {
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }

    LaunchedEffect(value) {
        if (textFieldValue.text != value) {
            textFieldValue = textFieldValue.copy(text = value, selection = TextRange(value.length))
        }
    }

    BasicTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            val isSelectionChangeOnly = newValue.text == textFieldValue.text
            val isDeletingText = newValue.text.length < textFieldValue.text.length
            val isWithinMaxLength = maxLength == null || newValue.text.length <= maxLength

            val shouldAllowChange = isSelectionChangeOnly || isDeletingText || isWithinMaxLength
            if (shouldAllowChange) {
                textFieldValue = newValue
                if (!isSelectionChangeOnly) onValueChange(newValue.text)
            }
        },
        modifier = modifier.focusRequester(focusRequester)
            .onFocusChanged { onFocusChange?.invoke(it.isFocused) }
            .fillMaxWidth(),
        textStyle = textStyle.copy(
            color = if (enabled) {
                textFieldColors.focusedTextColor
            } else {
                textFieldColors.focusedTextColor.copy(DISABLED_CONTENT_ALPHA)
            },
        ),
        enabled = enabled,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        keyboardOptions = createKeyboardOptions(capitalization, keyboardType, imeAction),
        keyboardActions = createKeyboardActions(keyboardController, onDoneClick),
        interactionSource = interactionSource,
    ) { innerTextField ->
        TextFieldDecorationBox(
            value = textFieldValue.text,
            innerTextField = innerTextField,
            visualTransformation = visualTransformation,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            showError = showError,
            maxLines = maxLines,
            interactionSource = interactionSource,
            textFieldColors = textFieldColors,
            singleLine = singleLine,
            enabled = enabled,
            shape = shape,
        )
    }
}

private fun createKeyboardOptions(
    capitalization: KeyboardCapitalization,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
) = KeyboardOptions.Default.copy(
    capitalization = capitalization,
    keyboardType = keyboardType,
    imeAction = imeAction,
)

private fun createKeyboardActions(
    keyboardController: SoftwareKeyboardController?,
    onDoneClick: () -> Unit,
) = KeyboardActions(
    onDone = {
        keyboardController?.hide()
        onDoneClick()
    },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextFieldDecorationBox(
    value: String,
    innerTextField: @Composable () -> Unit,
    visualTransformation: VisualTransformation,
    placeholder: String?,
    maxLines: Int,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    prefix: @Composable (() -> Unit)?,
    showError: Boolean,
    interactionSource: MutableInteractionSource,
    textFieldColors: TextFieldColors,
    singleLine: Boolean,
    enabled: Boolean,
    shape: Shape,
) {
    OutlinedTextFieldDefaults.DecorationBox(
        value = value,
        visualTransformation = visualTransformation,
        innerTextField = innerTextField,
        placeholder = placeholder?.let {
            {
                YVStoreInputPlaceholder(
                    text = it,
                    maxLines = maxLines,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        isError = showError,
        interactionSource = interactionSource,
        colors = textFieldColors,
        singleLine = singleLine,
        enabled = enabled,
        contentPadding = PaddingValues(12.dp),
        container = {
            OutlinedTextFieldContainer(
                enabled = enabled,
                interactionSource = interactionSource,
                shape = shape,
                showError = showError,
                textFieldColors = textFieldColors,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OutlinedTextFieldContainer(
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    shape: Shape,
    showError: Boolean,
    textFieldColors: TextFieldColors,
    modifier: Modifier = Modifier,
) {
    OutlinedTextFieldDefaults.Container(
        modifier = modifier,
        enabled = enabled,
        isError = showError,
        interactionSource = interactionSource,
        colors = textFieldColors,
        shape = shape,
        unfocusedBorderThickness = 1.dp,
        focusedBorderThickness = 1.dp,
    )
}

@Composable
fun YVStoreInputStatusRow(
    showError: Boolean,
    error: String?,
    maxLength: Int?,
    showMaxLengthCounter: Boolean,
    currentValue: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = showError,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            YVStoreInputError(text = error.orEmpty())
        }

        Spacer(modifier = Modifier.weight(1f))

        if (maxLength != null && showMaxLengthCounter) {
            YVStoreInputMaxLengthCounter(
                currentLength = currentValue.length,
                maxLength = maxLength,
            )
        }
    }
}

@Composable
fun YVStoreInputMaxLengthCounter(
    maxLength: Int,
    currentLength: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(top = 8.dp),
    ) {
        Text(
            text = "$currentLength / $maxLength",
            style = MaterialTheme.typography.labelSmall.copy(
                color = YVStoreTheme.colors.textColors.textSecondary,
            ),
        )
    }
}
