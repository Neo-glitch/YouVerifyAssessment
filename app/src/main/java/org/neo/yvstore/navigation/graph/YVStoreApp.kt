package org.neo.yvstore.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.neo.yvstore.AppAuthState
import org.neo.yvstore.MainViewModel
import org.neo.yvstore.core.ui.animations.EnterExitTransitions
import org.neo.yvstore.navigation.routes.AppRoute

@Composable
fun YVStoreApp(
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController()
) {

    val authState by viewModel.authState.collectAsStateWithLifecycle()

    if (authState == AppAuthState.LOADING) return

    val startDestination = if (authState == AppAuthState.AUTHENTICATED) {
        AppRoute.MainGraph
    } else {
        AppRoute.AuthGraph
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterExitTransitions.SlideInFromRight },
        exitTransition = { EnterExitTransitions.SlideOutToLeft },
        popEnterTransition = { EnterExitTransitions.SlideInFromLeft },
        popExitTransition = { EnterExitTransitions.SlideOutToRight },
    ) {
        authGraph(navController)

        mainGraph(navController)
    }
}