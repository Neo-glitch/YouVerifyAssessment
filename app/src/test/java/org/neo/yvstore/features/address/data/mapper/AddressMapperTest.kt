package org.neo.yvstore.features.address.data.mapper

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.neo.yvstore.core.database.model.AddressEntity
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.features.address.data.datasource.remote.model.AddressDto

class AddressMapperTest {

    @Test
    fun `toEntity should map AddressDto to AddressEntity`() {
        val dto = AddressDto(
            id = "a1",
            userId = "u1",
            streetAddress = "123 Main St",
            city = "Springfield",
            state = "IL",
            country = "US"
        )

        val entity = dto.toEntity()

        assertThat(entity.id).isEqualTo("a1")
        assertThat(entity.userId).isEqualTo("u1")
        assertThat(entity.streetAddress).isEqualTo("123 Main St")
        assertThat(entity.city).isEqualTo("Springfield")
        assertThat(entity.state).isEqualTo("IL")
        assertThat(entity.country).isEqualTo("US")
    }

    @Test
    fun `toAddress should map AddressEntity to Address`() {
        val entity = AddressEntity(
            id = "a1",
            userId = "u1",
            streetAddress = "123 Main St",
            city = "Springfield",
            state = "IL",
            country = "US"
        )

        val address = entity.toAddress()

        assertThat(address.id).isEqualTo("a1")
        assertThat(address.userId).isEqualTo("u1")
        assertThat(address.streetAddress).isEqualTo("123 Main St")
        assertThat(address.city).isEqualTo("Springfield")
        assertThat(address.state).isEqualTo("IL")
        assertThat(address.country).isEqualTo("US")
    }

    @Test
    fun `toEntity should map Address to AddressEntity`() {
        val address = Address(
            id = "a1",
            userId = "u1",
            streetAddress = "123 Main St",
            city = "Springfield",
            state = "IL",
            country = "US"
        )

        val entity = address.toEntity()

        assertThat(entity.id).isEqualTo("a1")
        assertThat(entity.userId).isEqualTo("u1")
        assertThat(entity.streetAddress).isEqualTo("123 Main St")
        assertThat(entity.city).isEqualTo("Springfield")
        assertThat(entity.state).isEqualTo("IL")
        assertThat(entity.country).isEqualTo("US")
    }
}
