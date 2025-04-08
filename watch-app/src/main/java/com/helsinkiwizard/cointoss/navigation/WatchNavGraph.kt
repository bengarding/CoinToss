package com.helsinkiwizard.cointoss.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.wear.compose.navigation.composable
import com.helsinkiwizard.cointoss.ui.AboutScreen
import com.helsinkiwizard.cointoss.ui.CoinTossScreen
import com.helsinkiwizard.cointoss.ui.WatchSettingsScreen
import com.helsinkiwizard.cointoss.ui.coinlist.CoinListScreen
import com.helsinkiwizard.cointoss.ui.composables.SpeedPicker
import com.helsinkiwizard.core.CoreConstants.VALUE_UNDEFINED

const val MAIN_ROUTE = "mainNavRoute"
const val SPEED_PICKER_RESULT = "pickerResult"

enum class NavRoute {
    Home,
    CoinList,
    Settings,
    About,
    Picker
}

// https://medium.com/androiddevelopers/navigation-compose-meet-type-safety-e081fb3cf2f8
fun NavGraphBuilder.mainGraph() {
    navigation(
        startDestination = NavRoute.Home.name,
        route = MAIN_ROUTE
    ) {
        composable(NavRoute.Home.name) {
            CoinTossScreen()
        }
        composable(NavRoute.CoinList.name) {
            CoinListScreen()
        }
        composable(NavRoute.Settings.name) {
            WatchSettingsScreen()
        }
        composable(NavRoute.About.name) {
            AboutScreen()
        }
        composable(
            route = NavRoute.Picker.name + "/{startValue}",
            arguments = listOf(navArgument("startValue") { type = NavType.FloatType })
        ) { backStackEntry ->
            val startValue = backStackEntry.arguments?.getFloat("startValue") ?: VALUE_UNDEFINED.toFloat()
            SpeedPicker(startValue)
        }
    }
}
