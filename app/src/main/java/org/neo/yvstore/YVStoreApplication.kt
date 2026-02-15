package org.neo.yvstore

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.neo.yvstore.core.cache.di.cacheModule
import org.neo.yvstore.core.data.di.dataModule
import org.neo.yvstore.core.database.di.databaseModule
import org.neo.yvstore.di.appModule
import org.neo.yvstore.features.address.di.addressModule
import org.neo.yvstore.features.auth.di.authModule
import org.neo.yvstore.features.cart.di.cartModule
import org.neo.yvstore.features.order.di.orderModule
import org.neo.yvstore.features.product.di.productModule

class YVStoreApplication : Application(), ImageLoaderFactory {
    private val imageLoader: ImageLoader by inject()

    private val appComponent: MutableList<Module> = mutableListOf(
        appModule,
        cacheModule,
        databaseModule,
        authModule,
        cartModule,
        productModule,
        addressModule,
        orderModule,
        dataModule,
    )

    /**
     * Provides the configured ImageLoader for Coil.
     * This makes the Koin-provided ImageLoader the app-wide default.
     */
    override fun newImageLoader(): ImageLoader {
        return imageLoader
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(
                appComponent
            )
        }

//        ProductSeeder.seed(Firebase.firestore)
    }
}