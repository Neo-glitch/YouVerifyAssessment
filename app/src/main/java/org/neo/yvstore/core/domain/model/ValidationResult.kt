package org.neo.yvstore.core.domain.model

/**
 * Sealed interface representing the result of a validation operation.
 */
sealed interface ValidationResult {
    /**
     * Indicates the input is valid and passes all validation checks.
     */
    data object Valid : ValidationResult

    /**
     * Indicates the input failed validation with a human-readable reason.
     *
     * @property reason A user-friendly message explaining why validation failed
     */
    data class Invalid(val reason: String) : ValidationResult
}
