package org.neo.yvstore.core.domain.validator

import org.neo.yvstore.core.domain.model.ValidationResult

/**
 * Validates email addresses.
 */
object EmailValidator {
    private val EMAIL_REGEX = Regex(
        "^[a-zA-Z0-9._+-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+$"
    )

    /**
     * Validates an email address.
     * Trims whitespace before validation.
     *
     * @param email The email address to validate
     * @return [ValidationResult.Valid] if the email is valid, [ValidationResult.Invalid] with a reason otherwise
     */
    fun validate(email: String): ValidationResult {
        val trimmedEmail = email.trim()

        return when {
            trimmedEmail.isBlank() -> ValidationResult.Invalid("Email is required")
            !EMAIL_REGEX.matches(trimmedEmail) -> ValidationResult.Invalid("Invalid email format")
            else -> ValidationResult.Valid
        }
    }

    fun String.isValidEmail(): Boolean = validate(this) is ValidationResult.Valid
}


