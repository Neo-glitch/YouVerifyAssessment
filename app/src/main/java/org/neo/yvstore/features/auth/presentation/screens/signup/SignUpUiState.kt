package org.neo.yvstore.features.auth.presentation.screens.signup

import org.neo.yvstore.core.domain.validator.EmailValidator.isValidEmail
import org.neo.yvstore.core.domain.validator.NameValidator.isValidName
import org.neo.yvstore.core.domain.validator.PasswordValidator.isValidPassword
import org.neo.yvstore.core.ui.model.TextInputFieldState

sealed interface SignUpLoadState {
    data object Idle : SignUpLoadState
    data object Loading : SignUpLoadState
    data class Error(val message: String) : SignUpLoadState
}

data class SignUpUiState(
    val email: TextInputFieldState = TextInputFieldState(),
    val firstName: TextInputFieldState = TextInputFieldState(),
    val lastName: TextInputFieldState = TextInputFieldState(),
    val password: TextInputFieldState = TextInputFieldState(),
    val confirmPassword: TextInputFieldState = TextInputFieldState(),
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val loadState: SignUpLoadState = SignUpLoadState.Idle,
) {
    val areAllInputsValid: Boolean
        get() = email.value.isValidEmail() &&
                firstName.value.isValidName() &&
                lastName.value.isValidName() &&
                password.value.isValidPassword() &&
                confirmPassword.value.isNotEmpty() &&
                confirmPassword.errorMsg == null &&
                password.value == confirmPassword.value
}
