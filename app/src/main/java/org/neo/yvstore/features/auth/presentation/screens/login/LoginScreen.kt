package org.neo.yvstore.features.auth.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import org.neo.yvstore.R
import org.neo.yvstore.core.designSystem.theme.YVStoreTheme
import org.neo.yvstore.core.ui.component.button.YVStorePrimaryButton
import org.neo.yvstore.core.ui.component.input.YVStoreInputSensitiveIcon
import org.neo.yvstore.core.ui.component.input.YVStoreTextInput
import org.neo.yvstore.core.ui.component.text.StyledPart
import org.neo.yvstore.core.ui.component.text.YVStoreMultiStyleText
import org.neo.yvstore.core.ui.extension.noRippleClearFocusClickable

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LoginScreen(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
        onLoginClick = viewModel::login,
        onCreateAccountClick = onNavigateToSignUp,
    )
}

@Composable
private fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        LoginHeader()

        Spacer(modifier = Modifier.height(48.dp))

        LoginForm(
            email = uiState.email,
            password = uiState.password,
            isPasswordVisible = uiState.isPasswordVisible,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
        )

        Spacer(modifier = Modifier.height(32.dp))

        LoginButton(
            isLoading = uiState.loginState is LoginState.Loading,
            onClick = onLoginClick,
        )

        Spacer(modifier = Modifier.height(24.dp))

        SignUpPrompt(onClick = onCreateAccountClick)

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun LoginHeader() {
    Image(
        painter = painterResource(R.drawable.ic_yv_store_logo),
        contentDescription = stringResource(R.string.login_logo_content_desc),
        modifier = Modifier.size(100.dp),
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        ),
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = stringResource(R.string.login_motto),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = YVStoreTheme.colors.textColors.textSecondary,
        ),
    )
}

@Composable
private fun LoginForm(
    email: String,
    password: String,
    isPasswordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
) {
    YVStoreTextInput(
        value = email,
        onValueChange = onEmailChange,
        label = stringResource(R.string.login_email_label),
        placeholder = stringResource(R.string.login_email_placeholder),
        keyboardType = KeyboardType.Email,
    )

    Spacer(modifier = Modifier.height(16.dp))

    YVStoreTextInput(
        value = password,
        onValueChange = onPasswordChange,
        label = stringResource(R.string.login_password_label),
        placeholder = stringResource(R.string.login_password_placeholder),
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
    )
}

@Composable
private fun LoginButton(
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    YVStorePrimaryButton(
        text = stringResource(R.string.login_button),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        loading = isLoading,
    )
}

@Composable
private fun SignUpPrompt(onClick: () -> Unit) {
    val fullText = stringResource(R.string.login_no_account_prompt)
    val createAccountText = stringResource(R.string.login_create_account_action)

    YVStoreMultiStyleText(
        fullText = fullText,
        modifier = Modifier.noRippleClearFocusClickable(
            enabled = true,
            onClick = onClick,
        ),
        parts = listOf(
            StyledPart(
                value = createAccountText,
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                ),
            ),
        ),
        defaultStyle = MaterialTheme.typography.bodyMedium.copy(
            color = YVStoreTheme.colors.textColors.textSecondary,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    YVStoreTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onCreateAccountClick = {},
        )
    }
}
