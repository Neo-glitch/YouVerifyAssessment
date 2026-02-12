package org.neo.yvstore.features.auth.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.auth.domain.repository.AuthRepository

/**
 * Use case for signing up a new user.
 * Pure delegation to the repository. Validation is handled by the ViewModel.
 *
 * @property repository The auth repository for performing sign-up operations
 */
class SignUpUseCase(
    private val repository: AuthRepository
) {
    /**
     * Signs up a new user with the provided credentials and profile information.
     *
     * @param email User's email address
     * @param password User's password
     * @param firstName User's first name
     * @param lastName User's last name
     * @return [Resource.Success] with Unit on successful sign-up, [Resource.Error] with operation failure message
     */
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
