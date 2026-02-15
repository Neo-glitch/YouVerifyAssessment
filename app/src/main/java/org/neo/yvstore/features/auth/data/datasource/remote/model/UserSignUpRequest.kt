package org.neo.yvstore.features.auth.data.datasource.remote.model

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.PropertyName

/**
 * Request model for user sign up in Firebase Firestore
 */
data class UserSignUpRequest(
    val uid: String,
    val email: String,
    @get:PropertyName("first_name")
    var firstName: String,
    @get:PropertyName("last_name")
    var lastName: String,
    val createdAt: Any = FieldValue.serverTimestamp()
)