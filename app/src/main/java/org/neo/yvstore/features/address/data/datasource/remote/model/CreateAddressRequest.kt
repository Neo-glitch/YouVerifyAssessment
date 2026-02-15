package org.neo.yvstore.features.address.data.datasource.remote.model

import com.google.firebase.firestore.PropertyName

data class CreateAddressRequest(
    val id: String = "",
    @get:PropertyName("user_id")
    val userId: String = "",
    @get:PropertyName("street_address")
    val streetAddress: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = ""
)