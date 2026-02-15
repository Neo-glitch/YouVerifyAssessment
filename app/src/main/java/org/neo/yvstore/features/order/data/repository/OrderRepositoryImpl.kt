package org.neo.yvstore.features.order.data.repository

import com.google.firebase.Timestamp
import org.neo.yvstore.core.common.util.ExceptionHandler
import org.neo.yvstore.core.domain.manager.UserManager
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.order.data.datasource.remote.OrderRemoteDatasource
import org.neo.yvstore.features.order.data.datasource.remote.model.CreateOrderRequest
import org.neo.yvstore.features.order.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val remoteDatasource: OrderRemoteDatasource,
    private val userManager: UserManager
) : OrderRepository {

    override suspend fun placeOrder(
        totalAmount: Double,
        shippingAddress: String,
        cartItemIds: List<String>
    ): Resource<String> {
        return try {
            val user = userManager.getUser()
            val userId = user?.uid ?: return Resource.Error("User not found")

            val now = Timestamp.now()
            val request = CreateOrderRequest(
                userId = userId,
                totalAmount = totalAmount,
                shippingAddress = shippingAddress,
                status = "confirmed",
                createdAt = now,
                updatedAt = now,
                cartItemId = cartItemIds
            )

            val orderId = remoteDatasource.createOrder(request)
            Resource.Success(orderId)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }
}
