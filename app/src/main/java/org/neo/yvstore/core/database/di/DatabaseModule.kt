package org.neo.yvstore.core.database.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.neo.yvstore.core.database.YVStoreDatabase

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            YVStoreDatabase::class.java,
            "yvstore_database"
        ).fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    single { get<YVStoreDatabase>().productDao() }
    single { get<YVStoreDatabase>().cartItemDao() }
    single { get<YVStoreDatabase>().addressDao() }
}
