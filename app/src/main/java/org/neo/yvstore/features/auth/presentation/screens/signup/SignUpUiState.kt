package org.neo.yvstore.features.auth.presentation.screens.signup

sealed interface SignUpState {
    data object Idle : SignUpState
    data object Loading : SignUpState
    data class Success(val message: String) : SignUpState
    data class Error(val message: String) : SignUpState
}

data class SignUpUiState(
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val signUpState: SignUpState = SignUpState.Idle,
)
