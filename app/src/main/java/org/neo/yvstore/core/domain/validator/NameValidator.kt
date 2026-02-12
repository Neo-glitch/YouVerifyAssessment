package org.neo.yvstore.core.domain.validator

import org.neo.yvstore.core.domain.model.ValidationResult

/**
 * Validates names (first name, last name).
 */
object NameValidator {
    private const val MIN_NAME_LENGTH = 2

    private val WHITESPACE_REGEX = Regex("\\s")
    private val STARTS_WITH_LETTER_REGEX = Regex("^[a-zA-Z]")
    private val ENDS_WITH_LETTER_REGEX = Regex("[a-zA-Z]$")
    private val VALID_CHARS_REGEX = Regex("^[a-zA-Z]+(['-][a-zA-Z]+)*$")

    /**
     * Validates a name.
     * Trims whitespace before validation.
     *
     * @return [ValidationResult.Valid] if the name is valid, [ValidationResult.Invalid] with a reason otherwise
     */
    fun String.validateName(): ValidationResult {
        val trimmedName = this.trim()

        return when {
            trimmedName.length < MIN_NAME_LENGTH -> ValidationResult.Invalid("Name is too short")
            WHITESPACE_REGEX.containsMatchIn(trimmedName) -> ValidationResult.Invalid("Name must be a single word")
            !STARTS_WITH_LETTER_REGEX.containsMatchIn(trimmedName) -> ValidationResult.Invalid("Name must start with a letter")
            !ENDS_WITH_LETTER_REGEX.containsMatchIn(trimmedName) -> ValidationResult.Invalid("Name must end with a letter")
            !VALID_CHARS_REGEX.matches(trimmedName) -> ValidationResult.Invalid("Name contains invalid characters")
            else -> ValidationResult.Valid
        }
    }

    /**
     * Convenience function to check if a name is valid.
     *
     * @return true if the name is valid, false otherwise
     */
    fun String.isValidName(): Boolean {
        return this.validateName() is ValidationResult.Valid
    }
}
