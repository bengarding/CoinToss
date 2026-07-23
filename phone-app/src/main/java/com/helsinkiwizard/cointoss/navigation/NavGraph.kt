package com.helsinkiwizard.cointoss.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.core.R as CoreR
import com.helsinkiwizard.cointoss.ui.AboutScreen
import com.helsinkiwizard.cointoss.ui.AttributionsScreen
import com.helsinkiwizard.cointoss.ui.CoinListScreen
import com.helsinkiwizard.cointoss.ui.CreateCoinScreen
import com.helsinkiwizard.cointoss.ui.HomeScreen
import com.helsinkiwizard.cointoss.ui.RemoveAdsScreen
import com.helsinkiwizard.cointoss.ui.SettingsScreen

const val MAIN_ROUTE = "mainNavRoute"

enum class NavRoute(
    @StringRes val titleRes: Int,
    val icon: ImageVector? = null,
    @DrawableRes val iconRes: Int? = null
) {
    Home(R.string.coin_toss, iconRes = R.drawable.ic_coin_toss),
    CoinList(R.string.select, Icons.Outlined.MonetizationOn),
    Settings(CoreR.string.settings, Icons.Outlined.Settings),
    About(CoreR.string.about, Icons.Outlined.Info),
    Attributions(R.string.attributions),
    CreateCoin(R.string.custom, Icons.Outlined.AddCircleOutline),
    RemoveAds(R.string.remove_ads, iconRes = R.drawable.ic_no_ads)
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
