package org.neo.yvstore

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.neo.yvstore.features.auth.di.authModule

class YVStoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(authModule)
        }
    }
}