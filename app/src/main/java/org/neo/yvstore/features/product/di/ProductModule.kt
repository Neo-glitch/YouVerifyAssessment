package org.neo.yvstore.features.product.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neo.yvstore.features.product.presentation.screen.productDetails.ProductDetailsViewModel

val productModule = module {
    // Presentation layer
    viewModelOf(::ProductDetailsViewModel)
}
