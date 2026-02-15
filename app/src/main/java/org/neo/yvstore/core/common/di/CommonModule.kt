package org.neo.yvstore.core.common.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.neo.yvstore.core.common.dispatcher.DispatcherProvider
import org.neo.yvstore.core.common.dispatcher.DispatcherProviderImpl

val commonModule = module {
    singleOf(::DispatcherProviderImpl) { bind<DispatcherProvider>() }
}