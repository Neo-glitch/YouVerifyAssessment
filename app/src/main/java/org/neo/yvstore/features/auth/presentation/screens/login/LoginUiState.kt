package org.neo.yvstore.features.auth.presentation.screens.login

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    data class Success(val message: String) : LoginState
    data class Error(val message: String) : LoginState
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val loginState: LoginState = LoginState.Idle,
)