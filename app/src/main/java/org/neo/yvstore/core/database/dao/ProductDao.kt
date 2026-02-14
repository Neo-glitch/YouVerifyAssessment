package org.neo.yvstore.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.neo.yvstore.core.database.model.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY created_at DESC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("""
        SELECT * FROM products
        ORDER BY created_at DESC
        LIMIT CASE WHEN :count IS NULL THEN -1 ELSE :count END
    """)
    fun observeProducts(count: Int?): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products ORDER BY created_at DESC")
    fun getProductsPaged(): PagingSource<Int, ProductEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clearAllProducts()
}