package org.neo.yvstore.features.auth.domain.model

/**
 * Domain model representing an authenticated user.
 *
 * @property uid Unique identifier from Firebase Auth
 * @property email User's email address
 * @property firstName User's first name
 * @property lastName User's last name
 */
data class User(
    val uid: String,
    val email: String,
    val firstName: String,
    val lastName: String
)
