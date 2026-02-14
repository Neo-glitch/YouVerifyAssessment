package org.neo.yvstore.features.address.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.neo.yvstore.features.address.data.datasource.remote.model.AddressDto

class AddressRemoteDatasourceImpl(
    private val firestore: FirebaseFirestore
) : AddressRemoteDatasource {

    override suspend fun getAddresses(userId: String): List<AddressDto> {
        val querySnapshot = firestore.collection("addresses")
            .whereEqualTo("user_id", userId)
            .get()
            .await()

        return querySnapshot.toObjects(AddressDto::class.java)
    }

    override suspend fun addAddress(userId: String, address: AddressDto): String {
        val documentRef = firestore.collection("addresses").document()
        val addressWithId = address.copy(id = documentRef.id, userId = userId)
        documentRef.set(addressWithId).await()
        return documentRef.id
    }

    override suspend fun deleteAddress(addressId: String) {
        firestore.collection("addresses")
            .document(addressId)
            .delete()
            .await()
    }
}
