package org.neo.yvstore.features.address.data.mapper

import org.neo.yvstore.core.database.model.AddressEntity
import org.neo.yvstore.features.address.data.datasource.remote.model.AddressDto
import org.neo.yvstore.core.domain.model.Address

fun AddressDto.toEntity(): AddressEntity {
    return AddressEntity(
        id = id,
        userId = userId,
        streetAddress = streetAddress,
        city = city,
        state = state,
        country = country
    )
}

fun AddressEntity.toAddress(): Address {
    return Address(
        id = id,
        userId = userId,
        streetAddress = streetAddress,
        city = city,
        state = state,
        country = country
    )
}

fun Address.toEntity(): AddressEntity {
    return AddressEntity(
        id = id,
        userId = userId,
        streetAddress = streetAddress,
        city = city,
        state = state,
        country = country
    )
}
