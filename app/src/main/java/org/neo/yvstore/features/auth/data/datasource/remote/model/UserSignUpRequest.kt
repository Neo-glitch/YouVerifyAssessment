package org.neo.yvstore.features.auth.data.datasource.remote.model

import com.google.firebase.firestore.FieldValue

/**
 * Request model for user sign up in Firebase Firestore
 */
data class UserSignUpRequest(
    val uid: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val createdAt: Any = FieldValue.serverTimestamp()
)
