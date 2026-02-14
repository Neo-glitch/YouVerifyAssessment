package org.neo.yvstore.features.address.presentation.screen.addressList

import org.neo.yvstore.features.address.presentation.model.AddressUi

data class AddressListUiState(
    val addresses: List<AddressUi> = emptyList(),
    val loadState: AddressListLoadState = AddressListLoadState.Loading,
    val deleteError: String? = null
)

sealed class AddressListLoadState {
    data object Loading : AddressListLoadState()
    data object Loaded : AddressListLoadState()
    data class Error(val message: String) : AddressListLoadState()
}
