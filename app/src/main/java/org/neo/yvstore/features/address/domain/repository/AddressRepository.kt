package org.neo.yvstore.features.address.domain.repository

import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.core.domain.model.Address

interface AddressRepository {
    fun getAddresses(): Flow<Resource<List<Address>>>
    suspend fun getAddressById(id: String): Resource<Address>
    suspend fun addAddress(address: Address): Resource<Unit>
    suspend fun deleteAddress(addressId: String): Resource<Unit>
    suspend fun refreshAddresses(): Resource<Unit>
}
