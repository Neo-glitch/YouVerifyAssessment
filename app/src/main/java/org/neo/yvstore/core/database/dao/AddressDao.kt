package org.neo.yvstore.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.database.model.AddressEntity

@Dao
interface AddressDao {

    @Query("SELECT * FROM addresses WHERE user_id = :userId ORDER BY id ASC")
    fun getByUserId(userId: String): Flow<List<AddressEntity>>

    @Query("SELECT * FROM addresses ORDER BY id ASC")
    fun getAll(): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(address: AddressEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(addresses: List<AddressEntity>)

    @Query("DELETE FROM addresses WHERE id = :addressId")
    suspend fun deleteById(addressId: String)

    @Query("DELETE FROM addresses WHERE user_id = :userId")
    suspend fun deleteAllByUserId(userId: String)

    @Query("DELETE FROM addresses")
    suspend fun deleteAll()
}
