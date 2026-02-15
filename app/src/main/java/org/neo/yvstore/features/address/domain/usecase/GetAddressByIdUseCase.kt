package org.neo.yvstore.features.address.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.features.address.domain.repository.AddressRepository

class GetAddressByIdUseCase(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(id: String): Resource<Address> = repository.getAddressById(id)
}
