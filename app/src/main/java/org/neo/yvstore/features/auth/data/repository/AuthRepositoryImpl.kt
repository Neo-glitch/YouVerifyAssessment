package org.neo.yvstore.features.auth.data.repository

import org.neo.yvstore.core.common.util.ExceptionHandler
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.auth.data.datasource.remote.AuthRemoteDatasource
import org.neo.yvstore.features.auth.data.mapper.toUser
import org.neo.yvstore.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDatasource: AuthRemoteDatasource,
) : AuthRepository {

    override suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Resource<Unit> {
        return try {
            remoteDatasource.signUp(email, password, firstName, lastName)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }

    override suspend fun signIn(email: String, password: String): Resource<User> {
        return try {
            val authUser = remoteDatasource.signIn(email, password)
            val user = authUser.toUser()
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }
}
