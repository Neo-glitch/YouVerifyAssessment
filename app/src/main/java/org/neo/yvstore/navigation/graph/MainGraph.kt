package org.neo.yvstore.navigation.graph

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import org.neo.yvstore.features.product.presentation.screen.productDetails.ProductDetailsScreen
import org.neo.yvstore.features.product.presentation.screen.productList.HomeProductListScreen
import org.neo.yvstore.navigation.routes.AppRoute
import org.neo.yvstore.navigation.routes.MainGraphRoute

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation<AppRoute.MainGraph>(
        startDestination = MainGraphRoute.HomeScreen,
    ) {
        composable<MainGraphRoute.HomeScreen> {
            HomeProductListScreen(
                onNavigateToCart = {
                    navController.navigate(MainGraphRoute.CartScreen)
                },
                onNavigateToSearch = {
                    // TODO: Implement search navigation
                },
                onNavigateToProductDetails = { productId ->
                    navController.navigate(MainGraphRoute.ProductDetailsScreen(productId))
                },
                onViewAllClick = {
                    // TODO: Implement view all navigation
                },
                hasCartItems = false,
            )
        }

        composable<MainGraphRoute.ProductDetailsScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<MainGraphRoute.ProductDetailsScreen>()
            ProductDetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<MainGraphRoute.CartScreen> {
            // TODO: Implement Cart Screen
            Spacer(modifier = Modifier.height(100.dp))
        }

        composable<MainGraphRoute.OrdersScreen> {
            // TODO: Implement Orders Screen
            Spacer(modifier = Modifier.height(100.dp))
        }

        composable<MainGraphRoute.ProfileScreen> {
            // TODO: Implement Profile Screen
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
