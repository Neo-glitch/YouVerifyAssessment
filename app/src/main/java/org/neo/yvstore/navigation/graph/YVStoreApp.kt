package org.neo.yvstore.navigation.graph

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.neo.yvstore.AppAuthState
import org.neo.yvstore.MainViewModel
import org.neo.yvstore.navigation.routes.AppRoute

@Composable
fun YVStoreApp(
    viewModel: MainViewModel,
    navController: NavHostController = rememberNavController()
) {

    val authState by viewModel.authState.collectAsStateWithLifecycle()

    val startDestination = if (authState == AppAuthState.AUTHENTICATED) {
        AppRoute.MainGraph
    } else {
        AppRoute.AuthGraph
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(navController)

        composable<AppRoute.MainGraph> {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}