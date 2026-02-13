package org.neo.yvstore.features.cart.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neo.yvstore.features.cart.presentation.screen.CartScreenViewModel

val cartModule = module {
    // Presentation layer
    viewModelOf(::CartScreenViewModel)
}
