package org.neo.yvstore.testdoubles.dao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.neo.yvstore.core.database.dao.AddressDao
import org.neo.yvstore.core.database.model.AddressEntity

class TestAddressDao : AddressDao {
    private val addresses = mutableListOf<AddressEntity>()
    private val addressesFlow = MutableSharedFlow<List<AddressEntity>>(replay = 1)

    init {
        addressesFlow.tryEmit(emptyList())
    }

    override fun observeAllAddresses(): Flow<List<AddressEntity>> = addressesFlow

    override suspend fun insertAddress(address: AddressEntity) {
        addresses.removeIf { it.id == address.id }
        addresses.add(address)
        addresses.sortBy { it.id }
        addressesFlow.emit(addresses.toList())
    }

    override suspend fun insertAddresses(addresses: List<AddressEntity>) {
        addresses.forEach { address ->
            this.addresses.removeIf { it.id == address.id }
            this.addresses.add(address)
        }
        this.addresses.sortBy { it.id }
        addressesFlow.emit(this.addresses.toList())
    }

    override suspend fun deleteAddressById(addressId: String) {
        addresses.removeIf { it.id == addressId }
        addressesFlow.emit(addresses.toList())
    }

    override suspend fun getAddressById(id: String): AddressEntity? = addresses.find { it.id == id }

    override suspend fun deleteAllAddresses() {
        addresses.clear()
        addressesFlow.emit(emptyList())
    }

    override suspend fun refreshAddresses(addresses: List<AddressEntity>) {
        deleteAllAddresses()
        insertAddresses(addresses)
    }
}
