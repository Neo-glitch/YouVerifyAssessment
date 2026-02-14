package org.neo.yvstore.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.neo.yvstore.core.database.dao.AddressDao
import org.neo.yvstore.core.database.dao.CartItemDao
import org.neo.yvstore.core.database.dao.ProductDao
import org.neo.yvstore.core.database.model.AddressEntity
import org.neo.yvstore.core.database.model.CartItemEntity
import org.neo.yvstore.core.database.model.ProductEntity

@Database(
    entities = [ProductEntity::class, CartItemEntity::class, AddressEntity::class],
    version = 3,
    exportSchema = true
)
abstract class YVStoreDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartItemDao(): CartItemDao
    abstract fun addressDao(): AddressDao
}
