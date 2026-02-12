package org.neo.yvstore.navigation.routes

import org.neo.yvstore.R

enum class BottomBarDestination(
    val screen: MainGraphRoute,
    val iconResId: Int,
    val labelResId: Int,
) {
    Home(
        screen = MainGraphRoute.HomeScreen,
        iconResId = R.drawable.ic_nav_home,
        labelResId = R.string.home_nav_label,
    ),

    Cart(
        screen = MainGraphRoute.CartScreen,
        iconResId = R.drawable.ic_nav_cart,
        labelResId = R.string.cart_nav_label,
    ),

    Orders(
        screen = MainGraphRoute.OrdersScreen,
        iconResId = R.drawable.ic_nav_orders,
        labelResId = R.string.orders_nav_label,
    ),

    Profile(
        screen = MainGraphRoute.ProfileScreen,
        iconResId = R.drawable.ic_nav_profile,
        labelResId = R.string.profile_nav_label,
    ),
}

val bottomBarDestinations = listOf(
    BottomBarDestination.Home,
    BottomBarDestination.Cart,
    BottomBarDestination.Orders,
    BottomBarDestination.Profile,
)
