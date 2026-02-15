package org.neo.yvstore.features.auth.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.neo.yvstore.core.domain.manager.UserManager
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.ValidationResult
import org.neo.yvstore.core.domain.validator.EmailValidator
import org.neo.yvstore.core.domain.validator.PasswordValidator
import org.neo.yvstore.features.auth.domain.usecase.LoginUseCase

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val userManager: UserManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<LoginUiEvent>()
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

    fun dismissError() {
        _uiState.update { it.copy(loadState = LoginLoadState.Idle) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        if (!_uiState.value.areAllInputsValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(loadState = LoginLoadState.Loading) }

            val result = loginUseCase(
                email = _uiState.value.email.value.trim(),
                password = _uiState.value.password.value.trim(),
            )

            when (result) {
                is Resource.Success -> {
                    _uiEvent.send(LoginUiEvent.LoginSuccess("Welcome back!"))
                    userManager.saveUser(result.data)
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(loadState = LoginLoadState.Error(result.message)) }
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

    private fun validatePassword(password: String): String? {
        return when (val result = PasswordValidator.validate(password)) {
            is ValidationResult.Invalid -> result.reason
            ValidationResult.Valid -> null
        }
    }
}
