package org.neo.yvstore.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.neo.yvstore.features.product.data.datasource.local.ProductDao
import org.neo.yvstore.features.product.data.datasource.local.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = true
)
abstract class YVStoreDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
