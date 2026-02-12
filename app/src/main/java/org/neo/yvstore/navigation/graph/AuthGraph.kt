package org.neo.yvstore.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.neo.yvstore.features.auth.presentation.screens.login.LoginScreen
import org.neo.yvstore.navigation.routes.AppRoute
import org.neo.yvstore.navigation.routes.AuthGraphRoute

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<AppRoute.AuthGraph>(
        startDestination = AuthGraphRoute.LoginScreen,
    ) {
        composable<AuthGraphRoute.LoginScreen> {
            LoginScreen(
                onNavigateToSignUp = {
                    // TODO: navigate to sign up screen
                },
            )
        }
    }
}
