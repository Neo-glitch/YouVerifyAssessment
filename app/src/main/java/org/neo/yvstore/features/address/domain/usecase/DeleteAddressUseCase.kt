package org.neo.yvstore.features.address.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.repository.AddressRepository

class DeleteAddressUseCase(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(addressId: String): Resource<Unit> {
        return repository.deleteAddress(addressId)
    }
}
