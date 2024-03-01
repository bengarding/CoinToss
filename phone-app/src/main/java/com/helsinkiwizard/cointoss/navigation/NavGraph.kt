package com.helsinkiwizard.cointoss.navigation

import androidx.compose.material3.DrawerState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.helsinkiwizard.cointoss.ui.CoinListScreen
import com.helsinkiwizard.cointoss.ui.HomeScreen
import com.helsinkiwizard.cointoss.ui.SettingsScreen

private const val MAIN_ROUTE = "mainNavRoute"

enum class NavRoute {
    Home,
    CoinList,
    Settings
}

fun NavGraphBuilder.mainGraph(drawerState: DrawerState) {
    navigation(
        startDestination = NavRoute.Home.name,
        route = MAIN_ROUTE
    ) {
        composable(NavRoute.Home.name) {
            HomeScreen(drawerState)
        }
        composable(NavRoute.CoinList.name) {
            CoinListScreen(drawerState)
        }
        composable(NavRoute.Settings.name) {
            SettingsScreen(drawerState)
        }
    }
}
