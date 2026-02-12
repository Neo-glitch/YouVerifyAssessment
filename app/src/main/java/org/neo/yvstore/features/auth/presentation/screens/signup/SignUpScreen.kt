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
import org.neo.yvstore.core.ui.component.surface.YVStoreScaffold
import org.neo.yvstore.core.ui.component.text.StyledPart
import org.neo.yvstore.core.ui.component.text.YVStoreMultiStyleText
import org.neo.yvstore.core.ui.extension.noRippleClearFocusClickable

@Composable
fun SignUpScreen(
    onNavigateBack: () -> Unit,
    viewModel: SignUpViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    SignUpScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onFirstNameChange = viewModel::onFirstNameChange,
        onLastNameChange = viewModel::onLastNameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
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
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
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
//                .padding(paddingValues)
                .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            SignUpBackButton(onClick = onNavigateBack)

            Spacer(modifier = Modifier.height(16.dp))
            SignUpHeader()

            Spacer(modifier = Modifier.height(32.dp))
            SignUpForm(
                email = uiState.email,
                firstName = uiState.firstName,
                lastName = uiState.lastName,
                password = uiState.password,
                confirmPassword = uiState.confirmPassword,
                isPasswordVisible = uiState.isPasswordVisible,
                isConfirmPasswordVisible = uiState.isConfirmPasswordVisible,
                onEmailChange = onEmailChange,
                onFirstNameChange = onFirstNameChange,
                onLastNameChange = onLastNameChange,
                onPasswordChange = onPasswordChange,
                onConfirmPasswordChange = onConfirmPasswordChange,
                onTogglePasswordVisibility = onTogglePasswordVisibility,
                onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
            )

            Spacer(modifier = Modifier.height(32.dp))
            SignUpButton(
                isLoading = uiState.signUpState is SignUpState.Loading,
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

@Composable
private fun SignUpForm(
    email: String,
    firstName: String,
    lastName: String,
    password: String,
    confirmPassword: String,
    isPasswordVisible: Boolean,
    isConfirmPasswordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
) {
    YVStoreTextInput(
        value = email,
        onValueChange = onEmailChange,
        label = stringResource(R.string.signup_email_label),
        placeholder = stringResource(R.string.signup_email_placeholder),
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(modifier = Modifier.fillMaxWidth()) {
        YVStoreTextInput(
            value = firstName,
            onValueChange = onFirstNameChange,
            label = stringResource(R.string.signup_first_name_label),
            placeholder = stringResource(R.string.signup_first_name_placeholder),
            modifier = Modifier.weight(1f),
            imeAction = ImeAction.Next,
        )

        Spacer(modifier = Modifier.width(12.dp))

        YVStoreTextInput(
            value = lastName,
            onValueChange = onLastNameChange,
            label = stringResource(R.string.signup_last_name_label),
            placeholder = stringResource(R.string.signup_last_name_placeholder),
            modifier = Modifier.weight(1f),
            imeAction = ImeAction.Next,
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    YVStoreTextInput(
        value = password,
        onValueChange = onPasswordChange,
        label = stringResource(R.string.signup_password_label),
        placeholder = stringResource(R.string.signup_password_placeholder),
        keyboardType = KeyboardType.Password,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            YVStoreInputSensitiveIcon(
                onClick = onTogglePasswordVisibility,
                showSensitiveInfo = isPasswordVisible,
            )
        },
        imeAction = ImeAction.Next,
    )

    Spacer(modifier = Modifier.height(16.dp))

    YVStoreTextInput(
        value = confirmPassword,
        onValueChange = onConfirmPasswordChange,
        label = stringResource(R.string.signup_confirm_password_label),
        placeholder = stringResource(R.string.signup_confirm_password_placeholder),
        keyboardType = KeyboardType.Password,
        visualTransformation = if (isConfirmPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            YVStoreInputSensitiveIcon(
                onClick = onToggleConfirmPasswordVisibility,
                showSensitiveInfo = isConfirmPasswordVisible,
            )
        },
        imeAction = ImeAction.Done,
    )
}

@Composable
private fun SignUpButton(
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    YVStorePrimaryButton(
        text = stringResource(R.string.signup_button),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        loading = isLoading,
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
            onFirstNameChange = {},
            onLastNameChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onTogglePasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {},
            onSignUpClick = {},
            onNavigateBack = {},
        )
    }
}
