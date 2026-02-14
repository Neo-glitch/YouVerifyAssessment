package org.neo.yvstore.features.address.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.model.Address
import org.neo.yvstore.features.address.domain.repository.AddressRepository

class AddAddressUseCase(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(address: Address): Resource<Unit> {
        return repository.addAddress(address)
    }
}
