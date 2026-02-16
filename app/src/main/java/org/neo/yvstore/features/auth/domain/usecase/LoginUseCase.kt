package org.neo.yvstore.features.auth.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.auth.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Resource<User> = repository.signIn(email = email, password = password)
}
