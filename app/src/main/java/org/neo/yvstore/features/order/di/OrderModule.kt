package org.neo.yvstore.features.order.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.neo.yvstore.features.order.data.datasource.remote.OrderRemoteDatasource
import org.neo.yvstore.features.order.data.datasource.remote.OrderRemoteDatasourceImpl
import org.neo.yvstore.features.order.data.repository.OrderRepositoryImpl
import org.neo.yvstore.features.order.domain.repository.OrderRepository
import org.neo.yvstore.features.order.domain.usecase.PlaceOrderUseCase
import org.neo.yvstore.features.order.presentation.screen.checkout.CheckoutViewModel

val orderModule = module {
    factoryOf(::OrderRemoteDatasourceImpl) { bind<OrderRemoteDatasource>() }
    factoryOf(::OrderRepositoryImpl) { bind<OrderRepository>() }

    factoryOf(::PlaceOrderUseCase)

    viewModel { params ->
        CheckoutViewModel(
            getCartItemsUseCase = get(),
            getAddressByIdUseCase = get(),
            placeOrderUseCase = get(),
            deleteAllCartItemsUseCase = get(),
            addressId = params.get()
        )
    }
}
