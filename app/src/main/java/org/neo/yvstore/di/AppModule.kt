package org.neo.yvstore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neo.yvstore.MainViewModel
import org.neo.yvstore.features.auth.presentation.screens.login.LoginViewModel

val appModule = module {
    single<FirebaseAuth> { Firebase.auth }
//    single<FirebaseFirestore> { Firebase.firestore }

    single {
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                .build()
        }
    }
    viewModelOf(::MainViewModel)
}