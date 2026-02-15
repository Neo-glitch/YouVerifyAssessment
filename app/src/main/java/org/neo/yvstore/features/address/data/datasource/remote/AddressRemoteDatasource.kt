package org.neo.yvstore.features.address.data.datasource.remote

import org.neo.yvstore.features.address.data.datasource.remote.model.AddressDto
import org.neo.yvstore.features.address.data.datasource.remote.model.CreateAddressRequest

interface AddressRemoteDatasource {
    suspend fun getAddresses(userId: String): List<AddressDto>
    suspend fun addAddress(userId: String, createAddressRequest: CreateAddressRequest): String
    suspend fun deleteAddress(addressId: String)
}
