package org.neo.yvstore.features.auth.presentation.screens.login

sealed interface LoginUiEvent {
    data class LoginSuccess(val message: String) : LoginUiEvent
}