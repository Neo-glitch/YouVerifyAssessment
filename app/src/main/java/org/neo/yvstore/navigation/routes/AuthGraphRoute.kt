package org.neo.yvstore.navigation.routes

import kotlinx.serialization.Serializable

sealed interface AuthGraphRoute {
    @Serializable
    data object LoginScreen : AuthGraphRoute

    @Serializable
    data object SignUpScreen : AuthGraphRoute
}
