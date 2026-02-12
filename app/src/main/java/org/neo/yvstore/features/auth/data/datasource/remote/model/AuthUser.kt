package org.neo.yvstore.features.auth.data.datasource.remote.model

/**
 * Remote data model representing authenticated user from Firebase
 * Default values are required for Firestore deserialization
 */
data class AuthUser(
    val uid: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = ""
)
