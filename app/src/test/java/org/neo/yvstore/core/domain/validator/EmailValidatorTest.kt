package org.neo.yvstore.core.domain.validator

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.neo.yvstore.core.domain.model.ValidationResult

class EmailValidatorTest {

    @Test
    fun `validate should return Invalid when email is blank`() {
        val result = EmailValidator.validate("   ")
        assertThat(result).isInstanceOf(ValidationResult.Invalid::class.java)
        assertThat((result as ValidationResult.Invalid).reason).isEqualTo("Email is required")
    }

    @Test
    fun `validate should return Invalid when email has no at sign`() {
        val result = EmailValidator.validate("userexample.com")
        assertThat(result).isInstanceOf(ValidationResult.Invalid::class.java)
        assertThat((result as ValidationResult.Invalid).reason).isEqualTo("Invalid email format")
    }

    @Test
    fun `validate should return Invalid when email has no domain`() {
        val result = EmailValidator.validate("user@")
        assertThat(result).isInstanceOf(ValidationResult.Invalid::class.java)
        assertThat((result as ValidationResult.Invalid).reason).isEqualTo("Invalid email format")
    }

    @Test
    fun `validate should return Valid for standard email`() {
        val result = EmailValidator.validate("user@example.com")
        assertThat(result).isEqualTo(ValidationResult.Valid)
    }

    @Test
    fun `validate should trim whitespace before validating`() {
        val result = EmailValidator.validate("  user@example.com  ")
        assertThat(result).isEqualTo(ValidationResult.Valid)
    }
}
