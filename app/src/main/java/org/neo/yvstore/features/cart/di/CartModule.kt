package org.neo.yvstore.features.cart.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neo.yvstore.features.cart.data.repository.CartRepositoryImpl
import org.neo.yvstore.features.cart.domain.repository.CartRepository
import org.neo.yvstore.features.cart.presentation.screen.CartScreenViewModel

val cartModule = module {
    // Data layer
    factoryOf(::CartRepositoryImpl) { bind<CartRepository>() }

    // Presentation layer
    viewModelOf(::CartScreenViewModel)
}
