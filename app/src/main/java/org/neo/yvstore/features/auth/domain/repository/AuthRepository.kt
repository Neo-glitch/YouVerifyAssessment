package org.neo.yvstore.features.auth.domain.repository

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User

/**
 * Repository interface for authentication operations.
 * Wraps data layer results in Resource for consistent error handling.
 */
interface AuthRepository {
    /**
     * Creates a new user account with Firebase Auth and stores user profile in Firestore.
     * Sends verification email and signs out the user to prevent auto-login.
     *
     * @param email User's email address
     * @param password User's password
     * @param firstName User's first name
     * @param lastName User's last name
     * @return Resource.Success(Unit) on success, Resource.Error with message on failure
     */
    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Resource<Unit>

    /**
     * Signs in an existing user with email and password.
     * Retrieves user profile from Firestore after successful authentication.
     *
     * @param email User's email address
     * @param password User's password
     * @return Resource.Success(User) on success, Resource.Error with message on failure
     */
    suspend fun signIn(email: String, password: String): Resource<User>
}