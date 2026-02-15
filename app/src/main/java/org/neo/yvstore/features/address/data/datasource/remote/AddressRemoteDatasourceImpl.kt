package org.neo.yvstore.features.address.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import org.neo.yvstore.core.common.dispatcher.DispatcherProvider
import org.neo.yvstore.core.network.utils.awaitWithTimeout
import org.neo.yvstore.features.address.data.datasource.remote.model.AddressDto
import org.neo.yvstore.features.address.data.datasource.remote.model.CreateAddressRequest

class AddressRemoteDatasourceImpl(
    private val firestore: FirebaseFirestore,
    private val dispatcherProvider: DispatcherProvider
) : AddressRemoteDatasource {

    override suspend fun getAddresses(userId: String): List<AddressDto> {
        val querySnapshot = firestore.collection("addresses")
            .whereEqualTo("user_id", userId)
            .get(Source.SERVER)
            .awaitWithTimeout(dispatcher = dispatcherProvider.io)

        return querySnapshot.toObjects(AddressDto::class.java)
    }

    override suspend fun addAddress(userId: String, createAddressRequest: CreateAddressRequest): String {
        val documentRef = firestore.collection("addresses").document()
        val addressWithId = createAddressRequest.copy(id = documentRef.id, userId = userId)
        documentRef.set(addressWithId).awaitWithTimeout(dispatcher = dispatcherProvider.io)
        return documentRef.id
    }

    override suspend fun deleteAddress(addressId: String) {
        firestore.collection("addresses")
            .document(addressId)
            .delete()
            .awaitWithTimeout(dispatcher = dispatcherProvider.io)
    }
}
