package org.neo.yvstore.features.auth.data.datasource.remote

import org.neo.yvstore.features.auth.data.datasource.remote.model.AuthUser

/**
 * Remote datasource interface for authentication operations using Firebase.
 * Methods throw exceptions on failure - repository layer handles error wrapping.
 */
interface AuthRemoteDatasource {
    /**
     * Creates a new user account with Firebase Auth and stores profile in Firestore.
     * Sends email verification and signs out the user.
     *
     * @param email User's email address
     * @param password User's password
     * @param firstName User's first name
     * @param lastName User's last name
     * @throws Exception on failure (auth error, network error, Firestore error)
     */
    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    )

    /**
     * Authenticates user with Firebase Auth and retrieves profile from Firestore.
     *
     * @param email User's email address
     * @param password User's password
     * @return AuthUser with profile data
     * @throws Exception on failure (auth error, network error, Firestore error)
     */
    suspend fun signIn(email: String, password: String): AuthUser
}