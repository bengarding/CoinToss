package com.helsinkiwizard.cointoss.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.navigation.MAIN_ROUTE
import com.helsinkiwizard.cointoss.navigation.NavRoute
import com.helsinkiwizard.cointoss.navigation.mainGraph
import com.helsinkiwizard.cointoss.theme.CoinTossTheme
import com.helsinkiwizard.cointoss.ui.drawer.DrawerContent
import com.helsinkiwizard.cointoss.ui.drawer.DrawerParams
import com.helsinkiwizard.core.CoreConstants.EXTRA_COIN_TYPE
import com.helsinkiwizard.core.coin.CoinType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repo: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoinTossTheme {
                CoinToss()
            }
        }
    }

    @Composable
    private fun CoinToss() {
        val navController: NavHostController = rememberNavController()
        val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        val initialCoinType = intent.extras?.getInt(EXTRA_COIN_TYPE) ?: CoinType.BITCOIN.value
        val coinType = CoinType.parse(
            repo.getCoinType.collectAsState(initial = initialCoinType).value
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            ModalNavigationDrawer(
//                modifier = Modifier
////                    .fillMaxWidth(.4f)
//                    .width(IntrinsicSize.Min),
                drawerState = drawerState,
                drawerContent = {
                    DrawerContent(
                        drawerState = drawerState,
                        menuItems = DrawerParams.drawerButtons,
                        defaultPick = NavRoute.Home,
                        onClick = { selectedRoute ->
                            when (selectedRoute) {
                                NavRoute.Home -> navController.navigate(selectedRoute.name)
                                NavRoute.CoinList -> navController.navigate(selectedRoute.name)
                                NavRoute.Settings -> navController.navigate(selectedRoute.name)
                            }

                        }
                    )
                }
            ) {
                NavHost(navController, startDestination = MAIN_ROUTE) {
                    mainGraph(drawerState)
                }
            }
        }
    }
}
