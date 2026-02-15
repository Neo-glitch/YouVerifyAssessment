package org.neo.yvstore.features.address.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.neo.yvstore.core.common.util.ExceptionHandler
import org.neo.yvstore.core.database.dao.AddressDao
import org.neo.yvstore.core.database.model.AddressEntity
import org.neo.yvstore.core.domain.manager.UserManager
import org.neo.yvstore.core.domain.model.Resource
import org.neo.yvstore.features.address.data.datasource.remote.AddressRemoteDatasource
import org.neo.yvstore.features.address.data.datasource.remote.model.AddressDto
import org.neo.yvstore.features.address.data.datasource.remote.model.CreateAddressRequest
import org.neo.yvstore.features.address.data.mapper.toAddress
import org.neo.yvstore.features.address.data.mapper.toEntity
import org.neo.yvstore.core.domain.model.Address
import org.neo.yvstore.features.address.domain.repository.AddressRepository

class AddressRepositoryImpl(
    private val remoteDatasource: AddressRemoteDatasource,
    private val addressDao: AddressDao,
    private val userManager: UserManager
) : AddressRepository {

    override fun getAddresses(): Flow<Resource<List<Address>>> {
        return addressDao.getAll()
            .map<List<AddressEntity>, Resource<List<Address>>> { entities ->
                Resource.Success(entities.map { it.toAddress() })
            }
            .catch { e ->
                emit(Resource.Error(ExceptionHandler.getErrorMessage(e)))
            }
    }

    override suspend fun getAddressById(id: String): Resource<Address> {
        return try {
            val entity = addressDao.getById(id)
                ?: return Resource.Error("Address not found")
            Resource.Success(entity.toAddress())
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }

    override suspend fun addAddress(address: Address): Resource<Unit> {
        return try {
            val user = userManager.getUser()
            val userId = user?.uid ?: return Resource.Error("User not found")

            val addressDto = CreateAddressRequest(
                streetAddress = address.streetAddress,
                city = address.city,
                state = address.state,
                country = address.country
            )
            val documentId = remoteDatasource.addAddress(userId, addressDto)

            val addressWithId = address.copy(id = documentId, userId = userId)
            addressDao.insert(addressWithId.toEntity())

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }

    override suspend fun deleteAddress(addressId: String): Resource<Unit> {
        return try {
            remoteDatasource.deleteAddress(addressId)
            addressDao.deleteById(addressId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }

    override suspend fun refreshAddresses(): Resource<Unit> {
        return try {
            val user = userManager.getUser()
            val userId = user?.uid ?: return Resource.Error("User not found")

            val addresses = remoteDatasource.getAddresses(userId)

            addressDao.deleteAll()
            addressDao.insertAll(addresses.map { it.toEntity() })
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(ExceptionHandler.getErrorMessage(e))
        }
    }
}
