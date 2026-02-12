package org.neo.yvstore.core.data.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.neo.yvstore.core.data.UserManagerImpl
import org.neo.yvstore.core.domain.manager.UserManager
import org.neo.yvstore.features.auth.data.repository.AuthRepositoryImpl
import org.neo.yvstore.features.auth.domain.repository.AuthRepository

val dataModule = module {
    singleOf(::UserManagerImpl) { bind<UserManager>() }
}