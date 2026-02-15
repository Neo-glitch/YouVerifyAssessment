package org.neo.yvstore.core.data.mapper

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.neo.yvstore.core.data.model.CacheUser
import org.neo.yvstore.core.domain.model.User

class CacheUserMapperTest {

    @Test
    fun `toCacheUser should map User to CacheUser`() {
        val user = User(
            uid = "uid-123",
            email = "john@example.com",
            firstName = "John",
            lastName = "Doe"
        )

        val cacheUser = user.toCacheUser()

        assertThat(cacheUser.uid).isEqualTo("uid-123")
        assertThat(cacheUser.email).isEqualTo("john@example.com")
        assertThat(cacheUser.firstName).isEqualTo("John")
        assertThat(cacheUser.lastName).isEqualTo("Doe")
    }

    @Test
    fun `toUser should map CacheUser to User`() {
        val cacheUser = CacheUser(
            uid = "uid-123",
            email = "john@example.com",
            firstName = "John",
            lastName = "Doe"
        )

        val user = cacheUser.toUser()

        assertThat(user.uid).isEqualTo("uid-123")
        assertThat(user.email).isEqualTo("john@example.com")
        assertThat(user.firstName).isEqualTo("John")
        assertThat(user.lastName).isEqualTo("Doe")
    }
}
