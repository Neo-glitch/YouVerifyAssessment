package org.neo.yvstore.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.database.model.AddressEntity

@Dao
interface AddressDao {

    @Query("SELECT * FROM addresses ORDER BY id ASC")
    fun observeAllAddresses(): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAddresses(addresses: List<AddressEntity>)

    @Query("DELETE FROM addresses WHERE id = :addressId")
    suspend fun deleteAddressById(addressId: String)

    @Query("SELECT * FROM addresses WHERE id = :id")
    suspend fun getAddressById(id: String): AddressEntity?

    @Query("DELETE FROM addresses")
    suspend fun deleteAllAddresses()

    @Transaction
    suspend fun refreshAddresses(addresses: List<AddressEntity>) {
        deleteAllAddresses()
        insertAddresses(addresses)
    }
}
