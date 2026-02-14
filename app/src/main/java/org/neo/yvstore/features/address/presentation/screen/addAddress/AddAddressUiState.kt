package org.neo.yvstore.features.address.presentation.screen.addAddress

import org.neo.yvstore.core.ui.model.TextInputFieldState

sealed interface AddAddressLoadStateState {
    data object Idle : AddAddressLoadStateState
    data object Saving : AddAddressLoadStateState
    data class Error(val message: String) : AddAddressLoadStateState
}

data class AddAddressUiState(
    val streetAddress: TextInputFieldState = TextInputFieldState(),
    val city: TextInputFieldState = TextInputFieldState(),
    val state: TextInputFieldState = TextInputFieldState(),
    val country: TextInputFieldState = TextInputFieldState(),
    val loadState: AddAddressLoadStateState = AddAddressLoadStateState.Idle,
) {
    val isFormValid: Boolean
        get() = streetAddress.value.isNotBlank() &&
                city.value.isNotBlank() &&
                state.value.isNotBlank() &&
                country.value.isNotBlank() &&
                streetAddress.errorMsg == null &&
                city.errorMsg == null &&
                state.errorMsg == null &&
                country.errorMsg == null
}
