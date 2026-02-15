package org.neo.yvstore.features.address.presentation.model

import org.neo.yvstore.core.domain.model.Address

data class AddressUi(
    val id: String,
    val streetAddress: String,
    val city: String,
    val state: String,
    val country: String,
    val formattedAddress: String
)

fun Address.toAddressUi(): AddressUi {
    return AddressUi(
        id = id,
        streetAddress = streetAddress,
        city = city,
        state = state,
        country = country,
        formattedAddress = "$streetAddress, $city, $state, $country"
    )
}
