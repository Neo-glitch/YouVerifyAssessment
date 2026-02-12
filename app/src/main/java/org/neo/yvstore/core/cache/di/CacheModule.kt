package org.neo.yvstore.core.cache.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.neo.yvstore.core.cache.AppCache
import org.neo.yvstore.core.cache.DataStoreCache

val cacheModule = module {
    single<AppCache> { DataStoreCache(androidContext()) }
}
