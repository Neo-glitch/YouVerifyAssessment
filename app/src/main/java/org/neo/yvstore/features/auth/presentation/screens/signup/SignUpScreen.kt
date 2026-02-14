package org.neo.yvstore.features.auth.presentation.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.button.YVStorePrimaryButton
import org.neo.yvstore.core.ui.component.input.YVStoreInputSensitiveIcon
import org.neo.yvstore.core.ui.component.input.YVStoreTextInput
import org.neo.yvstore.core.ui.component.dialog.YVStoreErrorDialog
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.core.ui.util.ObserveAsEvents
import org.neo.yvstore.core.ui.component.text.StyledPart
import org.neo.yvstore.core.ui.component.text.YVStoreMultiStyleText
import org.neo.yvstore.core.ui.extension.noRippleClearFocusClickable
import org.neo.yvstore.core.ui.model.TextInputFieldState

@Composable
fun SignUpScreen(
    onNavigateBack: () -> Unit,
    onSignUpSuccess: (String) -> Unit,
    viewModel: SignUpViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is SignUpEvent.Success -> onSignUpSuccess(event.message)
        }
    }

    if (uiState.signUpState is SignUpState.Error) {
        YVStoreErrorDialog(
            title = "Sign Up Failed",
            description = (uiState.signUpState as SignUpState.Error).message,
            onDismiss = viewModel::dismissError,
            onPrimaryButtonClick = viewModel::dismissError,
            primaryButtonText = "OK",
        )
    }

    SignUpScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onEmailBlur = viewModel::onEmailBlur,
        onFirstNameChange = viewModel::onFirstNameChange,
        onFirstNameBlur = viewModel::onFirstNameBlur,
        onLastNameChange = viewModel::onLastNameChange,
        onLastNameBlur = viewModel::onLastNameBlur,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordBlur = viewModel::onPasswordBlur,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onConfirmPasswordBlur = viewModel::onConfirmPasswordBlur,
        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
        onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
        onSignUpClick = viewModel::signUp,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
private fun SignUpScreen(
    uiState: SignUpUiState,
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
    onSignUpClick: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    YVStoreScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .imePadding()
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            SignUpBackButton(onClick = onNavigateBack)

            Spacer(modifier = Modifier.height(16.dp))
            SignUpHeader()

            Spacer(modifier = Modifier.height(32.dp))
            SignUpForm(
                email = uiState.email.value,
                firstName = uiState.firstName.value,
                lastName = uiState.lastName.value,
                password = uiState.password.value,
                confirmPassword = uiState.confirmPassword.value,
                isPasswordVisible = uiState.isPasswordVisible,
                isConfirmPasswordVisible = uiState.isConfirmPasswordVisible,
                emailError = uiState.email.errorMsg,
                firstNameError = uiState.firstName.errorMsg,
                lastNameError = uiState.lastName.errorMsg,
                passwordError = uiState.password.errorMsg,
                confirmPasswordError = uiState.confirmPassword.errorMsg,
                enabled = uiState.signUpState !is SignUpState.Loading,
                onEmailChange = onEmailChange,
                onEmailBlur = onEmailBlur,
                onFirstNameChange = onFirstNameChange,
                onFirstNameBlur = onFirstNameBlur,
                onLastNameChange = onLastNameChange,
                onLastNameBlur = onLastNameBlur,
                onPasswordChange = onPasswordChange,
                onPasswordBlur = onPasswordBlur,
                onConfirmPasswordChange = onConfirmPasswordChange,
                onConfirmPasswordBlur = onConfirmPasswordBlur,
                onTogglePasswordVisibility = onTogglePasswordVisibility,
                onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
            )

            Spacer(modifier = Modifier.height(32.dp))
            SignUpButton(
                isLoading = uiState.signUpState is SignUpState.Loading,
                enabled = uiState.areAllInputsValid,
                onClick = onSignUpClick,
            )

            Spacer(modifier = Modifier.height(24.dp))
            LoginPrompt(onClick = onNavigateBack)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SignUpBackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(
                color = YVStoreTheme.colors.navigationColors.appbarIconsBackground,
                shape = CircleShape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .size(24.dp),
            painter = painterResource(R.drawable.ic_back_arrow),
            contentDescription = stringResource(R.string.app_bar_nav_icon_content_desc),
            tint = YVStoreTheme.colors.navigationColors.navigationIcon,
        )
    }
}

@Composable
private fun SignUpHeader() {
    Column {
        Text(
            text = stringResource(R.string.signup_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.signup_subtitle),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = YVStoreTheme.colors.textColors.textSecondary,
            ),
        )
    }
}

@Composable
private fun SignUpForm(
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
        YVStoreTextInput(
            value = email,
            onValueChange = onEmailChange,
            onFocusChange = { isFocused ->
                if (!isFocused) onEmailBlur()
            },
            label = stringResource(R.string.signup_email_label),
            placeholder = stringResource(R.string.signup_email_placeholder),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            enabled = enabled,
            error = emailError,
            showError = emailError != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            YVStoreTextInput(
                value = firstName,
                onValueChange = onFirstNameChange,
                onFocusChange = { isFocused ->
                    if (!isFocused) onFirstNameBlur()
                },
                label = stringResource(R.string.signup_first_name_label),
                placeholder = stringResource(R.string.signup_first_name_placeholder),
                modifier = Modifier.weight(1f),
                imeAction = ImeAction.Next,
                enabled = enabled,
                error = firstNameError,
                showError = firstNameError != null
            )

            Spacer(modifier = Modifier.width(12.dp))

            YVStoreTextInput(
                value = lastName,
                onValueChange = onLastNameChange,
                onFocusChange = { isFocused ->
                    if (!isFocused) onLastNameBlur()
                },
                label = stringResource(R.string.signup_last_name_label),
                placeholder = stringResource(R.string.signup_last_name_placeholder),
                modifier = Modifier.weight(1f),
                imeAction = ImeAction.Next,
                enabled = enabled,
                error = lastNameError,
                showError = lastNameError != null
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        YVStoreTextInput(
            value = password,
            onValueChange = onPasswordChange,
            onFocusChange = { isFocused ->
                if (!isFocused) onPasswordBlur()
            },
            label = stringResource(R.string.signup_password_label),
            placeholder = stringResource(R.string.signup_password_placeholder),
            keyboardType = KeyboardType.Password,
            enabled = enabled,
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation(
                    mask = '*'
                )
            },
            trailingIcon = {
                YVStoreInputSensitiveIcon(
                    onClick = onTogglePasswordVisibility,
                    showSensitiveInfo = isPasswordVisible,
                )
            },
            imeAction = ImeAction.Next,
            error = passwordError,
            showError = passwordError != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        YVStoreTextInput(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            onFocusChange = { isFocused ->
                if (!isFocused) onConfirmPasswordBlur()
            },
            label = stringResource(R.string.signup_confirm_password_label),
            placeholder = stringResource(R.string.signup_confirm_password_placeholder),
            keyboardType = KeyboardType.Password,
            enabled = enabled,
            visualTransformation = if (isConfirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation(
                    mask = '*'
                )
            },
            trailingIcon = {
                YVStoreInputSensitiveIcon(
                    onClick = onToggleConfirmPasswordVisibility,
                    showSensitiveInfo = isConfirmPasswordVisible,
                )
            },
            imeAction = ImeAction.Done,
            error = confirmPasswordError,
            showError = confirmPasswordError != null
        )
    }
}

@Composable
private fun SignUpButton(
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    YVStorePrimaryButton(
        text = stringResource(R.string.signup_button),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        loading = isLoading,
        enabled = enabled,
    )
}

@Composable
private fun LoginPrompt(onClick: () -> Unit) {
    val fullText = stringResource(R.string.signup_has_account_prompt)
    val loginText = stringResource(R.string.signup_login_action)

    YVStoreMultiStyleText(
        fullText = fullText,
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClearFocusClickable(
                enabled = true,
                onClick = onClick,
            ),
        parts = listOf(
            StyledPart(
                value = loginText,
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                ),
            ),
        ),
        defaultStyle = MaterialTheme.typography.bodyMedium.copy(
            color = YVStoreTheme.colors.textColors.textSecondary,
            textAlign = TextAlign.Center
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    YVStoreTheme {
        SignUpScreen(
            uiState = SignUpUiState(),
            onEmailChange = {},
            onEmailBlur = {},
            onFirstNameChange = {},
            onFirstNameBlur = {},
            onLastNameChange = {},
            onLastNameBlur = {},
            onPasswordChange = {},
            onPasswordBlur = {},
            onConfirmPasswordChange = {},
            onConfirmPasswordBlur = {},
            onTogglePasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {},
            onSignUpClick = {},
            onNavigateBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenWithErrorsPreview() {
    YVStoreTheme {
        SignUpScreen(
            uiState = SignUpUiState(
                email = TextInputFieldState(
                    value = "bad",
                    hasLostFocus = true,
                    errorMsg = "Invalid email format"
                ),
                firstName = TextInputFieldState(
                    value = "A",
                    hasLostFocus = true,
                    errorMsg = "Name is too short"
                ),
                password = TextInputFieldState(
                    value = "123",
                    hasLostFocus = true,
                    errorMsg = "Password must be at least 6 characters"
                ),
                confirmPassword = TextInputFieldState(
                    value = "456",
                    hasLostFocus = true,
                    errorMsg = "Passwords do not match"
                )
            ),
            onEmailChange = {},
            onEmailBlur = {},
            onFirstNameChange = {},
            onFirstNameBlur = {},
            onLastNameChange = {},
            onLastNameBlur = {},
            onPasswordChange = {},
            onPasswordBlur = {},
            onConfirmPasswordChange = {},
            onConfirmPasswordBlur = {},
            onTogglePasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {},
            onSignUpClick = {},
            onNavigateBack = {},
        )
    }
}
