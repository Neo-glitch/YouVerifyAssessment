package org.neo.yvstore.features.order.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.neo.yvstore.features.order.data.datasource.remote.model.CreateOrderRequest

class OrderRemoteDatasourceImpl(
    private val firestore: FirebaseFirestore
) : OrderRemoteDatasource {

    override suspend fun createOrder(request: CreateOrderRequest): String {
        val documentRef = firestore.collection("orders").document()
        val orderWithId = request.copy(id = documentRef.id)
        documentRef.set(orderWithId).await()
        return documentRef.id
    }
}
