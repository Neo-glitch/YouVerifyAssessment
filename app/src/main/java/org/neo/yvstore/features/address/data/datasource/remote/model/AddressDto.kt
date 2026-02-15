package org.neo.yvstore.features.address.data.datasource.remote.model

import com.google.firebase.firestore.PropertyName

data class AddressDto(
    val id: String = "",
    @set:PropertyName("user_id")
    @get:PropertyName("user_id")
    var userId: String = "",
    @set:PropertyName("street_address")
    @get:PropertyName("street_address")
    var streetAddress: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = ""
)
