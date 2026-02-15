package org.neo.yvstore.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neo.yvstore.MainViewModel

val appModule = module {
    single<FirebaseAuth> { Firebase.auth }

    single {
        FirebaseFirestore.getInstance()
            .apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                .build()
        }
    }
    single { provideImageLoader(get()) }
    viewModelOf(::MainViewModel)
}

/**
 * Creates a configured ImageLoader instance for the application.
 *
 * Configuration:
 * - Memory cache: 25% of available app memory
 * - Disk cache: 50MB in app's cache directory
 * - Crossfade: 300ms transition
 * - Network cache policy: Enabled
 * - Disk cache policy: Enabled
 */
private fun provideImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25) // 25% of available app memory
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizeBytes(50 * 1024 * 1024) // 50MB
                .build()
        }
        .crossfade(300) // 300ms crossfade transition
        .respectCacheHeaders(false) // Ignore server cache headers for consistent behavior
        .build()
}