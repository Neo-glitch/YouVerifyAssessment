package org.neo.yvstore.core.domain.validator

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.neo.yvstore.core.domain.model.ValidationResult

class PasswordValidatorTest {

    @Test
    fun `validate should return Invalid when password is empty`() {
        val result = PasswordValidator.validate("")
        assertThat(result).isInstanceOf(ValidationResult.Invalid::class.java)
        assertThat((result as ValidationResult.Invalid).reason).isEqualTo("Password is required")
    }

    @Test
    fun `validate should return Invalid when password is shorter than 6 characters`() {
        val result = PasswordValidator.validate("abc12")
        assertThat(result).isInstanceOf(ValidationResult.Invalid::class.java)
        assertThat((result as ValidationResult.Invalid).reason).isEqualTo("Password must be at least 6 characters")
    }

    @Test
    fun `validate should return Valid when password is exactly 6 characters`() {
        val result = PasswordValidator.validate("abc123")
        assertThat(result).isEqualTo(ValidationResult.Valid)
    }

    @Test
    fun `validate should return Valid when password is longer than 6 characters`() {
        val result = PasswordValidator.validate("abcdef123")
        assertThat(result).isEqualTo(ValidationResult.Valid)
    }
}
