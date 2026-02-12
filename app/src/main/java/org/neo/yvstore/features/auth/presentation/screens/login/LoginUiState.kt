package org.neo.yvstore.features.auth.presentation.screens.login

import org.neo.yvstore.core.domain.validator.EmailValidator.isValidEmail
import org.neo.yvstore.core.domain.validator.PasswordValidator.isValidPassword
import org.neo.yvstore.core.ui.model.TextInputFieldState

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    data class Error(val message: String) : LoginState
}

sealed interface LoginUiEvent {
    data class LoginSuccess(val message: String) : LoginUiEvent
}

data class LoginUiState(
    val email: TextInputFieldState = TextInputFieldState(),
    val password: TextInputFieldState = TextInputFieldState(),
    val isPasswordVisible: Boolean = false,
    val loginState: LoginState = LoginState.Idle,
) {
    val areAllInputsValid: Boolean
        get() = email.value.isValidEmail() &&
                password.value.isValidPassword()
}