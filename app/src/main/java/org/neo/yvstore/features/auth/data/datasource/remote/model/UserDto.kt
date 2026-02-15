package org.neo.yvstore.features.auth.data.datasource.remote.model

import com.google.firebase.firestore.PropertyName


data class UserDto(
    val uid: String = "",
    val email: String = "",
    @set:PropertyName("first_name")
    var firstName: String = "",
    @set:PropertyName("last_name")
    var lastName: String = ""
)
