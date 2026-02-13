package org.neo.yvstore.navigation.routes

import kotlinx.serialization.Serializable

sealed interface MainGraphRoute {
    @Serializable
    data object HomeScreen : MainGraphRoute

    @Serializable
    data object CartScreen : MainGraphRoute

    @Serializable
    data object OrdersScreen : MainGraphRoute

    @Serializable
    data object ProfileScreen : MainGraphRoute

    @Serializable
    data class ProductDetailsScreen(val productId: String) : MainGraphRoute
}
