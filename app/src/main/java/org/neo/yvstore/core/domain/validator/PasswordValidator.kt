package org.neo.yvstore.core.domain.validator

import org.neo.yvstore.core.domain.model.ValidationResult

/**
 * Validates passwords.
 */
object PasswordValidator {
    private const val MIN_PASSWORD_LENGTH = 6

    /**
     * Validates a password.
     *
     * @param password The password to validate
     * @return [ValidationResult.Valid] if the password is valid, [ValidationResult.Invalid] with a reason otherwise
     */
    fun validate(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult.Invalid("Password is required")
            password.length < MIN_PASSWORD_LENGTH -> ValidationResult.Invalid("Password must be at least 6 characters")
            else -> ValidationResult.Valid
        }
    }

    fun String.isValidPassword(): Boolean = validate(this) is ValidationResult.Valid
}
