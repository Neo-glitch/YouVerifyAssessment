package org.neo.yvstore.features.auth.presentation.screens.signup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.neo.yvstore.R
import org.neo.yvstore.core.ui.component.input.YVStoreInputSensitiveIcon
import org.neo.yvstore.core.ui.component.input.YVStoreTextInput

@Composable
fun SignUpForm(
    email: String,
    firstName: String,
    lastName: String,
    password: String,
    confirmPassword: String,
    isPasswordVisible: Boolean,
    isConfirmPasswordVisible: Boolean,
    emailError: String?,
    firstNameError: String?,
    lastNameError: String?,
    passwordError: String?,
    confirmPasswordError: String?,
    enabled: Boolean,
    onEmailChange: (String) -> Unit,
    onEmailBlur: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onFirstNameBlur: () -> Unit,
    onLastNameChange: (String) -> Unit,
    onLastNameBlur: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordBlur: () -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onConfirmPasswordBlur: () -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
) {
    Column {
        EmailField(
            email = email,
            error = emailError,
            enabled = enabled,
            onValueChange = onEmailChange,
            onBlur = onEmailBlur,
        )

        Spacer(modifier = Modifier.height(16.dp))

        NameFields(
            firstName = firstName,
            lastName = lastName,
            firstNameError = firstNameError,
            lastNameError = lastNameError,
            enabled = enabled,
            onFirstNameChange = onFirstNameChange,
            onFirstNameBlur = onFirstNameBlur,
            onLastNameChange = onLastNameChange,
            onLastNameBlur = onLastNameBlur,
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            password = password,
            error = passwordError,
            enabled = enabled,
            isVisible = isPasswordVisible,
            onValueChange = onPasswordChange,
            onBlur = onPasswordBlur,
            onToggleVisibility = onTogglePasswordVisibility,
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConfirmPasswordField(
            confirmPassword = confirmPassword,
            error = confirmPasswordError,
            enabled = enabled,
            isVisible = isConfirmPasswordVisible,
            onValueChange = onConfirmPasswordChange,
            onBlur = onConfirmPasswordBlur,
            onToggleVisibility = onToggleConfirmPasswordVisibility,
        )
    }
}

@Composable
private fun EmailField(
    email: String,
    error: String?,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
    onBlur: () -> Unit,
) {
    YVStoreTextInput(
        value = email,
        onValueChange = onValueChange,
        onFocusChange = { isFocused -> if (!isFocused) onBlur() },
        label = stringResource(R.string.signup_email_label),
        placeholder = stringResource(R.string.signup_email_placeholder),
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        enabled = enabled,
        error = error,
        showError = error != null,
    )
}

@Composable
private fun NameFields(
    firstName: String,
    lastName: String,
    firstNameError: String?,
    lastNameError: String?,
    enabled: Boolean,
    onFirstNameChange: (String) -> Unit,
    onFirstNameBlur: () -> Unit,
    onLastNameChange: (String) -> Unit,
    onLastNameBlur: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        YVStoreTextInput(
            value = firstName,
            onValueChange = onFirstNameChange,
            onFocusChange = { isFocused -> if (!isFocused) onFirstNameBlur() },
            label = stringResource(R.string.signup_first_name_label),
            placeholder = stringResource(R.string.signup_first_name_placeholder),
            modifier = Modifier.weight(1f),
            imeAction = ImeAction.Next,
            enabled = enabled,
            error = firstNameError,
            showError = firstNameError != null,
        )

        Spacer(modifier = Modifier.width(12.dp))

        YVStoreTextInput(
            value = lastName,
            onValueChange = onLastNameChange,
            onFocusChange = { isFocused -> if (!isFocused) onLastNameBlur() },
            label = stringResource(R.string.signup_last_name_label),
            placeholder = stringResource(R.string.signup_last_name_placeholder),
            modifier = Modifier.weight(1f),
            imeAction = ImeAction.Next,
            enabled = enabled,
            error = lastNameError,
            showError = lastNameError != null,
        )
    }
}

@Composable
private fun PasswordField(
    password: String,
    error: String?,
    enabled: Boolean,
    isVisible: Boolean,
    onValueChange: (String) -> Unit,
    onBlur: () -> Unit,
    onToggleVisibility: () -> Unit,
) {
    YVStoreTextInput(
        value = password,
        onValueChange = onValueChange,
        onFocusChange = { isFocused -> if (!isFocused) onBlur() },
        label = stringResource(R.string.signup_password_label),
        placeholder = stringResource(R.string.signup_password_placeholder),
        keyboardType = KeyboardType.Password,
        enabled = enabled,
        visualTransformation = if (isVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation(mask = '*')
        },
        trailingIcon = {
            YVStoreInputSensitiveIcon(
                onClick = onToggleVisibility,
                showSensitiveInfo = isVisible,
            )
        },
        imeAction = ImeAction.Next,
        error = error,
        showError = error != null,
    )
}

@Composable
private fun ConfirmPasswordField(
    confirmPassword: String,
    error: String?,
    enabled: Boolean,
    isVisible: Boolean,
    onValueChange: (String) -> Unit,
    onBlur: () -> Unit,
    onToggleVisibility: () -> Unit,
) {
    YVStoreTextInput(
        value = confirmPassword,
        onValueChange = onValueChange,
        onFocusChange = { isFocused -> if (!isFocused) onBlur() },
        label = stringResource(R.string.signup_confirm_password_label),
        placeholder = stringResource(R.string.signup_confirm_password_placeholder),
        keyboardType = KeyboardType.Password,
        enabled = enabled,
        visualTransformation = if (isVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation(mask = '*')
        },
        trailingIcon = {
            YVStoreInputSensitiveIcon(
                onClick = onToggleVisibility,
                showSensitiveInfo = isVisible,
            )
        },
        imeAction = ImeAction.Done,
        error = error,
        showError = error != null,
    )
}
