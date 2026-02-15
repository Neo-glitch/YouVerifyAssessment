package org.neo.yvstore.features.address.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.features.address.domain.repository.AddressRepository

class GetAddressesUseCase(
    private val repository: AddressRepository
) {
    operator fun invoke(): Flow<Resource<List<Address>>> = repository.getAddresses()
}
