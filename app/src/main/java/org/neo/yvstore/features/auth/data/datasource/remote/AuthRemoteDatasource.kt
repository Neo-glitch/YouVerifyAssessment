package org.neo.yvstore.features.auth.data.datasource.remote

import org.neo.yvstore.features.auth.data.datasource.remote.model.UserDto

interface AuthRemoteDatasource {
    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    )

    suspend fun signIn(email: String, password: String): UserDto
}