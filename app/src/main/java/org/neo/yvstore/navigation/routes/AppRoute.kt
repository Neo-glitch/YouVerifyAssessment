package org.neo.yvstore.navigation.routes

import kotlinx.serialization.Serializable

sealed interface AppRoute {
    @Serializable
    data object AuthGraph : AppRoute

    @Serializable
    data object MainGraph : AppRoute
}