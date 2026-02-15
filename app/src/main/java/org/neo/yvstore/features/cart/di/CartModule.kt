package org.neo.yvstore.features.cart.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.neo.yvstore.features.cart.data.repository.CartRepositoryImpl
import org.neo.yvstore.features.cart.domain.repository.CartRepository
import org.neo.yvstore.features.cart.domain.usecase.AddCartItemUseCase
import org.neo.yvstore.features.cart.domain.usecase.DeleteAllCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.DeleteCartItemUseCase
import org.neo.yvstore.features.cart.domain.usecase.GetCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemByProductIdUseCase
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemCountUseCase
import org.neo.yvstore.features.cart.domain.usecase.ObserveCartItemsUseCase
import org.neo.yvstore.features.cart.domain.usecase.UpdateCartItemQuantityUseCase
import org.neo.yvstore.features.cart.presentation.screen.cartList.CartScreenViewModel

val cartModule = module {
    factoryOf(::CartRepositoryImpl) { bind<CartRepository>() }

    factoryOf(::ObserveCartItemCountUseCase)
    factoryOf(::ObserveCartItemByProductIdUseCase)
    factoryOf(::ObserveCartItemsUseCase)
    factoryOf(::GetCartItemsUseCase)
    factoryOf(::AddCartItemUseCase)
    factoryOf(::UpdateCartItemQuantityUseCase)
    factoryOf(::DeleteCartItemUseCase)
    factoryOf(::DeleteAllCartItemsUseCase)

    viewModelOf(::CartScreenViewModel)
}
