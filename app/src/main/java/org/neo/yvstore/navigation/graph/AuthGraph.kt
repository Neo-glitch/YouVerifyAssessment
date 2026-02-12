package org.neo.yvstore.navigation.graph

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.neo.yvstore.features.auth.presentation.screens.login.LoginScreen
import org.neo.yvstore.features.auth.presentation.screens.signup.SignUpScreen
import org.neo.yvstore.navigation.routes.AppRoute
import org.neo.yvstore.navigation.routes.AuthGraphRoute

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<AppRoute.AuthGraph>(
        startDestination = AuthGraphRoute.LoginScreen,
    ) {
        composable<AuthGraphRoute.LoginScreen> {
            val context = LocalContext.current
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(AuthGraphRoute.SignUpScreen)
                },
                onLoginSuccess = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    navController.navigate(AppRoute.MainGraph) {
                        popUpTo(AppRoute.AuthGraph) { inclusive = true }
                    }
                }
            )
        }

        composable<AuthGraphRoute.SignUpScreen> {
            val context = LocalContext.current
            SignUpScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSignUpSuccess = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            )
        }
    }
}
