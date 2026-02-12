package org.neo.yvstore.features.auth.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.User
import org.neo.yvstore.features.auth.domain.repository.AuthRepository

/**
 * Use case for logging in an existing user.
 * Pure delegation to the repository. Validation is handled by the ViewModel.
 *
 * @property repository The auth repository for performing login operations
 */
class LoginUseCase(
    private val repository: AuthRepository
) {
    /**
     * Logs in a user with the provided credentials.
     *
     * @param email User's email address
     * @param password User's password
     * @return [Resource.Success] with User on successful login, [Resource.Error] with operation failure message
     */
    suspend operator fun invoke(
        email: String,
        password: String,
    ): Resource<User> = repository.signIn(email = email, password = password)
}
