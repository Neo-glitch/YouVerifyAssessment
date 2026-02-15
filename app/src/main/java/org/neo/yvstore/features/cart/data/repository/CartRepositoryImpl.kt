package org.neo.yvstore.features.cart.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.neo.yvstore.core.common.util.ExceptionHandler
import org.neo.yvstore.features.cart.data.mapper.toCartItem
import org.neo.yvstore.core.database.dao.CartItemDao
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.domain.model.CartItem
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.cart.domain.repository.CartRepository

class CartRepositoryImpl(
    private val cartItemDao: CartItemDao
) : CartRepository {

    override fun observeCartItems(): Flow<Resource<List<CartItem>>> {
        return cartItemDao.observeAllCartItems()
            .map<List<CartItemEntity>, Resource<List<CartItem>>> { entities ->
                Resource.Success(entities.map { it.toCartItem() })
            }
            .catch { e ->
                emit(Resource.Error(ExceptionHandler.getErrorMessage(e)))
            }
    }

    override suspend fun getCartItems(): Resource<List<CartItem>> {
        return try {
            val entities = cartItemDao.getCartItems()
            Resource.Success(entities.map { it.toCartItem() })
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }

    override fun observeCartItemCount(): Flow<Resource<Int>> {
        return cartItemDao.observeCartItemCount()
            .map<Int, Resource<Int>> { count ->
                Resource.Success(count)
            }
            .catch { e ->
                emit(Resource.Error(ExceptionHandler.getErrorMessage(e)))
            }
    }

    override suspend fun addCartItem(cartItem: CartItemEntity): Resource<Unit> {
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

    override suspend fun deleteCartItem(id: Long): Resource<Unit> {
        return try {
            cartItemDao.deleteCartItem(id)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }

    override suspend fun deleteAllCartItems(): Resource<Unit> {
        return try {
            cartItemDao.deleteAllCartItems()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }

    override fun observeCartItemByProductId(productId: String): Flow<Resource<CartItem?>> {
        return cartItemDao.observeCartItemByProductId(productId)
            .map<CartItemEntity?, Resource<CartItem?>> { entity ->
                Resource.Success(entity?.toCartItem())
            }
            .catch { e ->
                emit(Resource.Error(ExceptionHandler.getErrorMessage(e)))
            }
    }
}
