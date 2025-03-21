package com.helsinkiwizard.cointoss.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.ui.AboutScreen
import com.helsinkiwizard.cointoss.ui.AttributionsScreen
import com.helsinkiwizard.cointoss.ui.CoinListScreen
import com.helsinkiwizard.cointoss.ui.CreateCoinScreen
import com.helsinkiwizard.cointoss.ui.HomeScreen
import com.helsinkiwizard.cointoss.ui.RemoveAdsScreen
import com.helsinkiwizard.cointoss.ui.SettingsScreen

const val MAIN_ROUTE = "mainNavRoute"

enum class NavRoute(val titleRes: Int) {
    Home(R.string.home),
    CoinList(R.string.choose_a_coin),
    Settings(R.string.settings),
    About(R.string.about),
    Attributions(R.string.attributions),
    CreateCoin(R.string.create_a_coin),
    RemoveAds(R.string.remove_ads)
}

fun NavGraphBuilder.mainGraph() {
    navigation(
        startDestination = NavRoute.Home.name,
        route = MAIN_ROUTE
    ) {
        composable(NavRoute.Home.name) {
            HomeScreen()
        }
        composable(NavRoute.CoinList.name) {
            CoinListScreen()
        }
        composable(NavRoute.Settings.name) {
            SettingsScreen()
        }
        composable(NavRoute.About.name) {
            AboutScreen()
        }
        composable(NavRoute.Attributions.name) {
            AttributionsScreen()
        }
        composable(NavRoute.CreateCoin.name) {
            CreateCoinScreen()
        }
        composable(NavRoute.RemoveAds.name) {
            RemoveAdsScreen()
        }
    }
}
