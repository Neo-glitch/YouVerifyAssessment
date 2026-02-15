package org.neo.yvstore.features.address.presentation.screen.addressList

sealed class AddressListUiEvent {
    data class ShowToast(val message: String) : AddressListUiEvent()
}
