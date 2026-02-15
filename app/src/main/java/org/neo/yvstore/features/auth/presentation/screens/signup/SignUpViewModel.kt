package org.neo.yvstore.features.auth.presentation.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neo.yvstore.core.common.util.capitalizeFirst
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.ValidationResult
import org.neo.yvstore.core.domain.validator.EmailValidator
import org.neo.yvstore.core.domain.validator.NameValidator.validateName
import org.neo.yvstore.core.domain.validator.PasswordValidator
import org.neo.yvstore.features.auth.domain.usecase.SignUpUseCase

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<SignUpEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            val currentEmail = it.email
            it.copy(
                email = currentEmail.copy(
                    value = email,
                    hasBeenModified = true,
                    errorMsg = if (currentEmail.hasLostFocus) {
                        validateEmail(email)
                    } else {
                        null
                    },
                ),
            )
        }
    }

    fun onEmailBlur() {
        if (!_uiState.value.email.hasBeenModified) return
        _uiState.update {
            it.copy(
                email = it.email.copy(
                    hasLostFocus = true,
                    errorMsg = validateEmail(it.email.value),
                ),
            )
        }
    }

    fun onFirstNameChange(firstName: String) {
        _uiState.update {
            val currentFirstName = it.firstName
            it.copy(
                firstName = currentFirstName.copy(
                    value = firstName,
                    hasBeenModified = true,
                    errorMsg = if (currentFirstName.hasLostFocus) {
                        validateName(firstName)
                    } else {
                        null
                    },
                ),
            )
        }
    }

    fun onFirstNameBlur() {
        if (!_uiState.value.firstName.hasBeenModified) return
        _uiState.update {
            it.copy(
                firstName = it.firstName.copy(
                    hasLostFocus = true,
                    errorMsg = validateName(it.firstName.value),
                ),
            )
        }
    }

    fun onLastNameChange(lastName: String) {
        _uiState.update {
            val currentLastName = it.lastName
            it.copy(
                lastName = currentLastName.copy(
                    value = lastName,
                    hasBeenModified = true,
                    errorMsg = if (currentLastName.hasLostFocus) {
                        validateName(lastName)
                    } else {
                        null
                    },
                ),
            )
        }
    }

    fun onLastNameBlur() {
        if (!_uiState.value.lastName.hasBeenModified) return
        _uiState.update {
            it.copy(
                lastName = it.lastName.copy(
                    hasLostFocus = true,
                    errorMsg = validateName(it.lastName.value),
                ),
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            val currentPassword = it.password
            it.copy(
                password = currentPassword.copy(
                    value = password,
                    hasBeenModified = true,
                    errorMsg = if (currentPassword.hasLostFocus) {
                        validatePassword(password)
                    } else {
                        null
                    },
                ),
            )
        }

        if (_uiState.value.confirmPassword.hasBeenModified) {
            _uiState.update {
                val currentConfirmPassword = it.confirmPassword
                it.copy(
                    confirmPassword = currentConfirmPassword.copy(
                        hasBeenModified = true,
                        errorMsg = if (currentConfirmPassword.hasLostFocus) {
                            validateConfirmPassword(currentConfirmPassword.value)
                        } else {
                            null
                        },
                    ),
                )
            }
        }
    }

    fun onPasswordBlur() {
        if (!_uiState.value.password.hasBeenModified) return
        _uiState.update {
            it.copy(
                password = it.password.copy(
                    hasLostFocus = true,
                    errorMsg = validatePassword(it.password.value),
                ),
            )
        }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update {
            val currentConfirmPassword = it.confirmPassword
            it.copy(
                confirmPassword = currentConfirmPassword.copy(
                    value = confirmPassword,
                    hasBeenModified = true,
                    errorMsg = if (currentConfirmPassword.hasLostFocus) {
                        validateConfirmPassword(confirmPassword)
                    } else {
                        null
                    },
                ),
            )
        }
    }

    fun onConfirmPasswordBlur() {
        if (!_uiState.value.confirmPassword.hasBeenModified) return
        _uiState.update {
            it.copy(
                confirmPassword = it.confirmPassword.copy(
                    hasLostFocus = true,
                    errorMsg = validateConfirmPassword(it.confirmPassword.value),
                ),
            )
        }
    }

    fun dismissError() {
        _uiState.update { it.copy(loadState = SignUpLoadState.Idle) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun signUp() {
        if (!_uiState.value.areAllInputsValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(loadState = SignUpLoadState.Loading) }

            val result = signUpUseCase(
                email = _uiState.value.email.value.trim(),
                password = _uiState.value.password.value.trim(),
                firstName = _uiState.value.firstName.value.trim().capitalizeFirst(),
                lastName = _uiState.value.lastName.value.trim().capitalizeFirst(),
            )

            when (result) {
                is Resource.Success -> {
                    _uiEvent.send(SignUpEvent.Success("Account created successfully."))
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(loadState = SignUpLoadState.Error(result.message)) }
                }
            }
        }
    }

    private fun validateEmail(email: String): String? {
        return when (val result = EmailValidator.validate(email)) {
            is ValidationResult.Invalid -> result.reason
            ValidationResult.Valid -> null
        }
    }

    private fun validateName(name: String): String? {
        return when (val result = name.validateName()) {
            is ValidationResult.Invalid -> result.reason
            ValidationResult.Valid -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when (val result = PasswordValidator.validate(password)) {
            is ValidationResult.Invalid -> result.reason
            ValidationResult.Valid -> null
        }
    }

    private fun validateConfirmPassword(confirmPassword: String): String? {
        if (confirmPassword.isEmpty()) return "Please confirm your password"
        if (confirmPassword != _uiState.value.password.value) return "Passwords do not match"
        return null
    }
}
