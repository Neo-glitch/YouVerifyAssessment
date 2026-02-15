package org.neo.yvstore.core.domain.validator

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.neo.yvstore.core.domain.model.ValidationResult
import org.neo.yvstore.core.domain.validator.NameValidator.validateName

class NameValidatorTest {

    @Test
    fun `validateName should return Invalid when name is shorter than 2 characters`() {
        val result = "A".validateName()
        assertThat(result).isInstanceOf(ValidationResult.Invalid::class.java)
        assertThat((result as ValidationResult.Invalid).reason).isEqualTo("Name is too short")
    }

    @Test
    fun `validateName should return Invalid when name contains whitespace`() {
        val result = "John Doe".validateName()
        assertThat(result).isInstanceOf(ValidationResult.Invalid::class.java)
        assertThat((result as ValidationResult.Invalid).reason).isEqualTo("Name must be a single word")
    }

    @Test
    fun `validateName should return Invalid when name starts with non-letter`() {
        val result = "1John".validateName()
        assertThat(result).isInstanceOf(ValidationResult.Invalid::class.java)
        assertThat((result as ValidationResult.Invalid).reason).isEqualTo("Name must start with a letter")
    }

    @Test
    fun `validateName should return Invalid when name ends with non-letter`() {
        val result = "John1".validateName()
        assertThat(result).isInstanceOf(ValidationResult.Invalid::class.java)
        assertThat((result as ValidationResult.Invalid).reason).isEqualTo("Name must end with a letter")
    }

    @Test
    fun `validateName should return Invalid when name contains invalid characters`() {
        val result = "Jo@hn".validateName()
        assertThat(result).isInstanceOf(ValidationResult.Invalid::class.java)
        assertThat((result as ValidationResult.Invalid).reason).isEqualTo("Name contains invalid characters")
    }

    @Test
    fun `validateName should return Valid for simple name`() {
        val result = "John".validateName()
        assertThat(result).isEqualTo(ValidationResult.Valid)
    }

    @Test
    fun `validateName should return Valid for hyphenated name`() {
        val result = "Mary-Jane".validateName()
        assertThat(result).isEqualTo(ValidationResult.Valid)
    }

    @Test
    fun `validateName should return Valid for name with apostrophe`() {
        val result = "O'Brien".validateName()
        assertThat(result).isEqualTo(ValidationResult.Valid)
    }
}
