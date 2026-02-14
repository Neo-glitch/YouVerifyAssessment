package org.neo.yvstore.features.address.presentation.screen.addAddress

sealed class AddAddressUiEvent {
    data object SaveSuccess : AddAddressUiEvent()
}
