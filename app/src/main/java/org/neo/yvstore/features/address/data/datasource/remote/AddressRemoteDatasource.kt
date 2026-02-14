package org.neo.yvstore.features.address.data.datasource.remote

import org.neo.yvstore.features.address.data.datasource.remote.model.AddressDto

interface AddressRemoteDatasource {
    suspend fun getAddresses(userId: String): List<AddressDto>
    suspend fun addAddress(userId: String, address: AddressDto): String
    suspend fun deleteAddress(addressId: String)
}
