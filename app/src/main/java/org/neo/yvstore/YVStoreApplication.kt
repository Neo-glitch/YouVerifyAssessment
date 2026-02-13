package org.neo.yvstore

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.neo.yvstore.core.cache.di.cacheModule
import org.neo.yvstore.core.data.di.dataModule
import org.neo.yvstore.di.appModule
import org.neo.yvstore.features.auth.di.authModule
import org.neo.yvstore.features.product.di.productModule

class YVStoreApplication : Application() {

    private val appComponent: MutableList<Module> = mutableListOf(
        appModule,
        cacheModule,
        authModule,
        productModule,
        dataModule,
    )

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(
                appComponent
            )
        }
    }
}