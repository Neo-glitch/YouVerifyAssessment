package org.neo.yvstore.testdoubles.datasource

import org.neo.yvstore.features.address.data.datasource.remote.AddressRemoteDatasource
import org.neo.yvstore.features.address.data.datasource.remote.model.AddressDto
import org.neo.yvstore.features.address.data.datasource.remote.model.CreateAddressRequest

class TestAddressRemoteDatasource : AddressRemoteDatasource {
    var addresses: List<AddressDto> = emptyList()
    var addAddressResult: String = "test-address-id"
    var error: Exception? = null

    override suspend fun getAddresses(userId: String): List<AddressDto> {
        error?.let { throw it }
        return addresses.filter { it.userId == userId }
    }

    override suspend fun addAddress(userId: String, createAddressRequest: CreateAddressRequest): String {
        error?.let { throw it }
        return addAddressResult
    }

    override suspend fun deleteAddress(addressId: String) {
        error?.let { throw it }
    }
}
