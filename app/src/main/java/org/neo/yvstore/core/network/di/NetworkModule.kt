package org.neo.yvstore.core.network.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val networkModule = module {
    single<FirebaseAuth> { Firebase.auth }

    single {
        FirebaseFirestore.getInstance()
            .apply {
                firestoreSettings = FirebaseFirestoreSettings.Builder()
                    .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                    .build()
            }
    }
}