package org.neo.yvstore.core.ui.model

/**
 * Represents the state of a text input field with validation.
 *
 * @property value The current text value
 * @property hasBeenModified Whether the user has modified this field
 * @property hasLostFocus Whether the field has lost focus at least once
 * @property errorMsg The current validation error message, if any
 */
data class TextInputFieldState(
    val value: String = "",
    val hasBeenModified: Boolean = false,
    val hasLostFocus: Boolean = false,
    val errorMsg: String? = null,
)