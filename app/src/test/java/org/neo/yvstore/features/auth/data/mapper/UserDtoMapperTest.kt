package org.neo.yvstore.features.auth.data.mapper

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.neo.yvstore.features.auth.data.datasource.remote.model.UserDto

class UserDtoMapperTest {

    @Test
    fun `toUser should map UserDto fields to User`() {
        val dto = UserDto(
            uid = "uid-123",
            email = "john@example.com",
            firstName = "John",
            lastName = "Doe"
        )

        val user = dto.toUser()

        assertThat(user.uid).isEqualTo("uid-123")
        assertThat(user.email).isEqualTo("john@example.com")
        assertThat(user.firstName).isEqualTo("John")
        assertThat(user.lastName).isEqualTo("Doe")
    }
}
