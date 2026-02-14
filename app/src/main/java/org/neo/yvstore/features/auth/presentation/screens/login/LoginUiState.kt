package org.neo.yvstore.features.auth.presentation.screens.login

import org.neo.yvstore.core.domain.validator.EmailValidator.isValidEmail
import org.neo.yvstore.core.domain.validator.PasswordValidator.isValidPassword
import org.neo.yvstore.core.ui.model.TextInputFieldState

sealed interface LoginLoadState {
    data object Idle : LoginLoadState
    data object Loading : LoginLoadState
    data class Error(val message: String) : LoginLoadState
}

data class LoginUiState(
    val email: TextInputFieldState = TextInputFieldState(),
    val password: TextInputFieldState = TextInputFieldState(),
    val isPasswordVisible: Boolean = false,
    val loadState: LoginLoadState = LoginLoadState.Idle,
) {
    val areAllInputsValid: Boolean
        get() = email.value.isValidEmail() &&
                password.value.isValidPassword()
}