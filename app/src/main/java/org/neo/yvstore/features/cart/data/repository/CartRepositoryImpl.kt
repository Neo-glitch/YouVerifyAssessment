package org.neo.yvstore.features.cart.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.neo.yvstore.core.common.util.ExceptionHandler
import org.neo.yvstore.core.data.mapper.toCartItem
import org.neo.yvstore.core.database.dao.CartItemDao
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

class CartRepositoryImpl(
    private val cartItemDao: CartItemDao
) : CartRepository {

    override fun getCartItems(): Flow<Resource<List<CartItem>>> {
        return cartItemDao.getAllCartItems()
            .map<List<CartItemEntity>, Resource<List<CartItem>>> { entities ->
                Resource.Success(entities.map { it.toCartItem() })
            }
            .catch { e ->
                emit(Resource.Error(ExceptionHandler.getErrorMessage(e)))
            }
    }

    override suspend fun addItem(cartItem: CartItemEntity): Resource<Unit> {
        return try {
            cartItemDao.insertCartItem(cartItem)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }

    override suspend fun updateQuantity(id: Long, quantity: Int): Resource<Unit> {
        return try {
            cartItemDao.updateQuantity(id, quantity)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }

    override suspend fun deleteItem(id: Long): Resource<Unit> {
        return try {
            cartItemDao.deleteCartItem(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }

    override suspend fun deleteAllItems(): Resource<Unit> {
        return try {
            cartItemDao.deleteAllCartItems()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }
}
