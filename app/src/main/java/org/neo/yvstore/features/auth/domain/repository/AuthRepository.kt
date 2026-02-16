package org.neo.yvstore.features.auth.domain.repository

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User

interface AuthRepository {
    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Resource<Unit>

    suspend fun signIn(email: String, password: String): Resource<User>
}