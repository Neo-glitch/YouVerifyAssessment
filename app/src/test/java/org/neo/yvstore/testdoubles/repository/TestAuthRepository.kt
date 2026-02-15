package org.neo.yvstore.testdoubles.repository

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.auth.domain.repository.AuthRepository

class TestAuthRepository : AuthRepository {
    var signInResult: Resource<User> = Resource.Error("Not configured")
    var signUpResult: Resource<Unit> = Resource.Error("Not configured")

    override suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Resource<Unit> = signUpResult

    override suspend fun signIn(email: String, password: String): Resource<User> = signInResult
}
