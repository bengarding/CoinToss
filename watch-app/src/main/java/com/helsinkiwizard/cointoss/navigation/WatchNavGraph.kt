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
import com.helsinkiwizard.cointoss.ui.composables.BEZEL_SENSITIVITY_TYPE
import com.helsinkiwizard.cointoss.ui.composables.BezelSensitivityPicker
import com.helsinkiwizard.cointoss.ui.composables.SPEED_TYPE
import com.helsinkiwizard.cointoss.ui.composables.SpeedPicker
import com.helsinkiwizard.core.CoreConstants.EMPTY_STRING
import com.helsinkiwizard.core.CoreConstants.VALUE_UNDEFINED

const val MAIN_ROUTE = "mainNavRoute"
const val SPEED_PICKER_RESULT = "speedPickerResult"
const val BEZEL_SENSITIVITY_PICKER_RESULT = "bezelSensitivityPickerResult"

enum class NavRoute {
    Home,
    CoinList,
    Settings,
    About,
    Picker
}

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
            route = NavRoute.Picker.name + "/{pickerType}/{startValue}",
            arguments = listOf(
                navArgument("pickerType") { type = NavType.StringType },
                navArgument("startValue") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pickerType = backStackEntry.arguments?.getString("pickerType") ?: EMPTY_STRING
            val startValue = backStackEntry.arguments?.getString("startValue") ?: EMPTY_STRING
            when (pickerType) {
                SPEED_TYPE -> SpeedPicker(startValue.toFloatOrNull() ?: VALUE_UNDEFINED.toFloat())
                BEZEL_SENSITIVITY_TYPE -> BezelSensitivityPicker(startValue.toIntOrNull() ?: VALUE_UNDEFINED)
            }
        }
    }
}
