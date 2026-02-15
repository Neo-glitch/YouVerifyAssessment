package org.neo.yvstore.navigation.graph

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import org.neo.yvstore.features.address.presentation.screen.addAddress.AddAddressScreen
import org.neo.yvstore.features.address.presentation.screen.addressList.AddressListScreen
import org.neo.yvstore.features.cart.presentation.screen.cartList.CartScreen
import org.neo.yvstore.features.order.presentation.screen.checkout.CheckoutScreen
import org.neo.yvstore.features.order.presentation.screen.orderSuccess.OrderSuccessScreen
import org.neo.yvstore.features.product.presentation.screen.allProductList.AllProductListScreen
import org.neo.yvstore.features.product.presentation.screen.productDetails.ProductDetailsScreen
import org.neo.yvstore.features.product.presentation.screen.productList.HomeProductListScreen
import org.neo.yvstore.features.product.presentation.screen.searchProductList.SearchProductListScreen
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
                    navController.navigate(MainGraphRoute.SearchScreen)
                },
                onNavigateToProductDetails = { productId ->
                    navController.navigate(MainGraphRoute.ProductDetailsScreen(productId))
                },
                onViewAllClick = {
                    navController.navigate(MainGraphRoute.AllProductsScreen)
                },
            )
        }

        composable<MainGraphRoute.SearchScreen> {
            SearchProductListScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onProductClick = { productId ->
                    navController.navigate(MainGraphRoute.ProductDetailsScreen(productId))
                },
            )
        }

        composable<MainGraphRoute.AllProductsScreen> {
            AllProductListScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onProductClick = { productId ->
                    navController.navigate(MainGraphRoute.ProductDetailsScreen(productId))
                },
            )
        }

        composable<MainGraphRoute.ProductDetailsScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<MainGraphRoute.ProductDetailsScreen>()
            ProductDetailsScreen(
                productId = route.productId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<MainGraphRoute.CartScreen> {
            CartScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onCheckout = {
                    navController.navigate(MainGraphRoute.AddressListScreen)
                }
            )
        }

        composable<MainGraphRoute.AddressListScreen> {
            AddressListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAddAddress = {
                    navController.navigate(MainGraphRoute.AddAddressScreen)
                },
                onAddressSelected = { addressId ->
                    navController.navigate(MainGraphRoute.CheckoutScreen(addressId))
                }
            )
        }

        composable<MainGraphRoute.AddAddressScreen> {
            AddAddressScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<MainGraphRoute.CheckoutScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<MainGraphRoute.CheckoutScreen>()
            CheckoutScreen(
                addressId = route.addressId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onOrderSuccess = {
                    navController.navigate(MainGraphRoute.OrderSuccessScreen) {
                        popUpTo(MainGraphRoute.CartScreen) { inclusive = false }
                    }
                }
            )
        }

        composable<MainGraphRoute.OrderSuccessScreen> {
            OrderSuccessScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinueShopping = {
                    navController.navigate(MainGraphRoute.HomeScreen) {
                        popUpTo(MainGraphRoute.HomeScreen) { inclusive = true }
                    }
                }
            )
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
