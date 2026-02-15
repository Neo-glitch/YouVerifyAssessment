package org.neo.yvstore.testdoubles.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.domain.repository.AddressRepository

class TestAddressRepository : AddressRepository {
    private val addresses = mutableListOf<Address>()
    private val addressesFlow = MutableSharedFlow<List<Address>>(replay = 1)

    var addResult: Resource<Unit> = Resource.Success(Unit)
    var deleteResult: Resource<Unit> = Resource.Success(Unit)
    var refreshResult: Resource<Unit> = Resource.Success(Unit)

    init {
        addressesFlow.tryEmit(emptyList())
    }

    override fun getAddresses(): Flow<Resource<List<Address>>> {
        return addressesFlow.map { Resource.Success(it) }
    }

    override suspend fun getAddressById(id: String): Resource<Address> {
        return addresses.find { it.id == id }?.let { Resource.Success(it) }
            ?: Resource.Error("Address not found")
    }

    override suspend fun addAddress(address: Address): Resource<Unit> {
        if (addResult is Resource.Success) {
            addresses.add(address)
            addressesFlow.emit(addresses.toList())
        }
        return addResult
    }

    override suspend fun deleteAddress(addressId: String): Resource<Unit> {
        if (deleteResult is Resource.Success) {
            addresses.removeIf { it.id == addressId }
            addressesFlow.emit(addresses.toList())
        }
        return deleteResult
    }

    override suspend fun refreshAddresses(): Resource<Unit> = refreshResult

    suspend fun emit(addresses: List<Address>) {
        this.addresses.clear()
        this.addresses.addAll(addresses)
        addressesFlow.emit(addresses)
    }
}
