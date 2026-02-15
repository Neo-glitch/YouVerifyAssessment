package org.neo.yvstore

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.neo.yvstore.core.cache.di.cacheModule
import org.neo.yvstore.core.data.di.dataModule
import org.neo.yvstore.core.data.seeder.ProductSeeder
import org.neo.yvstore.core.database.di.databaseModule
import org.neo.yvstore.di.appModule
import org.neo.yvstore.features.address.di.addressModule
import org.neo.yvstore.features.auth.di.authModule
import org.neo.yvstore.features.cart.di.cartModule
import org.neo.yvstore.features.order.di.orderModule
import org.neo.yvstore.features.product.di.productModule

class YVStoreApplication : Application() {

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