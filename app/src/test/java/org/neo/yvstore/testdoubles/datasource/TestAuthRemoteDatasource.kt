package org.neo.yvstore.testdoubles.datasource

import org.neo.yvstore.features.auth.data.datasource.remote.AuthRemoteDatasource
import org.neo.yvstore.features.auth.data.datasource.remote.model.UserDto

class TestAuthRemoteDatasource : AuthRemoteDatasource {
    var signInResult: UserDto? = null
    var signInError: Exception? = null
    var signUpError: Exception? = null

    override suspend fun signUp(email: String, password: String, firstName: String, lastName: String) {
        signUpError?.let { throw it }
    }

    override suspend fun signIn(email: String, password: String): UserDto {
        signInError?.let { throw it }
        return signInResult ?: throw IllegalStateException("signInResult not configured")
    }
}
