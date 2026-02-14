package org.neo.yvstore.features.product.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neo.yvstore.features.product.data.datasource.remote.ProductRemoteDatasource
import org.neo.yvstore.features.product.data.datasource.remote.ProductRemoteDatasourceImpl
import org.neo.yvstore.features.product.data.repository.ProductRepositoryImpl
import org.neo.yvstore.features.product.domain.repository.ProductRepository
import org.neo.yvstore.features.product.domain.usecase.ObserveProductsUseCase
import org.neo.yvstore.features.product.domain.usecase.RefreshProductsUseCase
import org.neo.yvstore.features.product.domain.usecase.SearchProductsUseCase
import org.neo.yvstore.features.product.presentation.screen.productDetails.ProductDetailsViewModel
import org.neo.yvstore.features.product.presentation.screen.productList.HomeProductListViewModel
import org.neo.yvstore.features.product.presentation.screen.searchProductList.SearchProductListViewModel

val productModule = module {
    // Data layer
    factoryOf(::ProductRemoteDatasourceImpl) { bind<ProductRemoteDatasource>() }
    factoryOf(::ProductRepositoryImpl) { bind<ProductRepository>() }

    // Domain layer - Use cases
    factoryOf(::ObserveProductsUseCase)
    factoryOf(::RefreshProductsUseCase)
    factoryOf(::SearchProductsUseCase)

    // Presentation layer
    viewModelOf(::ProductDetailsViewModel)
    viewModelOf(::HomeProductListViewModel)
    viewModelOf(::SearchProductListViewModel)
}
