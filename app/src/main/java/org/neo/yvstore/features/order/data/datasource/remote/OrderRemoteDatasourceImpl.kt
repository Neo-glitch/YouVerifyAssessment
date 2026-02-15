package org.neo.yvstore.features.order.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import org.neo.yvstore.core.common.dispatcher.DispatcherProvider
import org.neo.yvstore.core.network.utils.awaitWithTimeout
import org.neo.yvstore.features.order.data.datasource.remote.model.CreateOrderRequest

class OrderRemoteDatasourceImpl(
    private val firestore: FirebaseFirestore,
    private val dispatcherProvider: DispatcherProvider
) : OrderRemoteDatasource {

    override suspend fun createOrder(request: CreateOrderRequest): String {
        val documentRef = firestore.collection("orders").document()
        val orderWithId = request.copy(id = documentRef.id)
        documentRef.set(orderWithId).awaitWithTimeout(dispatcher = dispatcherProvider.io)
        return documentRef.id
    }
}
