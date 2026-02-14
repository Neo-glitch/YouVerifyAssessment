package org.neo.yvstore.features.auth.presentation.screens.signup

sealed interface SignUpEvent {
    data class Success(val message: String) : SignUpEvent
}
