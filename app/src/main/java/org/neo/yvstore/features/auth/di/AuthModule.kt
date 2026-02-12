package org.neo.yvstore.features.auth.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neo.yvstore.features.auth.data.datasource.remote.AuthRemoteDatasource
import org.neo.yvstore.features.auth.data.datasource.remote.AuthRemoteDatasourceImpl
import org.neo.yvstore.features.auth.data.repository.AuthRepositoryImpl
import org.neo.yvstore.features.auth.domain.repository.AuthRepository
import org.neo.yvstore.features.auth.presentation.screens.login.LoginViewModel
import org.neo.yvstore.features.auth.presentation.screens.signup.SignUpViewModel

val authModule = module {
    // Data layer
    factoryOf(::AuthRemoteDatasourceImpl) { bind<AuthRemoteDatasource>() }
    factoryOf(::AuthRepositoryImpl) { bind<AuthRepository>() }


    // Presentation layer
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
}
