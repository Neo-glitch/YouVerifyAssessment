package org.neo.yvstore.features.address.presentation.screen.addressList

sealed class AddressListUiEvent {
    data class Error(val message: String) : AddressListUiEvent()
}
