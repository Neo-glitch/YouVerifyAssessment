package org.neo.yvstore.features.auth.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.auth.domain.repository.AuthRepository

class SignUpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ): Resource<Unit> = repository.signUp(
        email = email,
        password = password,
        firstName = firstName,
        lastName = lastName
    )
}
