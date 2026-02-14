package org.neo.yvstore.features.address.domain.usecase

import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.repository.AddressRepository

class RefreshAddressesUseCase(
    private val repository: AddressRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.refreshAddresses()
    }
}
