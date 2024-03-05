package com.helsinkiwizard.cointoss.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.helsinkiwizard.cointoss.Constants.NAV_TRANSITION_DURATION
import com.helsinkiwizard.cointoss.R
import com.helsinkiwizard.cointoss.Repository
import com.helsinkiwizard.cointoss.navigation.MAIN_ROUTE
import com.helsinkiwizard.cointoss.navigation.NavRoute
import com.helsinkiwizard.cointoss.navigation.mainGraph
import com.helsinkiwizard.cointoss.theme.CoinTossTheme
import com.helsinkiwizard.cointoss.ui.drawer.DrawerContent
import com.helsinkiwizard.core.CoreConstants.EXTRA_COIN_TYPE
import com.helsinkiwizard.core.coin.CoinType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CoinToss() {
        val navController: NavHostController = rememberNavController()
        val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        val initialCoinType = intent.extras?.getInt(EXTRA_COIN_TYPE) ?: CoinType.BITCOIN.value
        val coinType = CoinType.parse(
            repo.getCoinType.collectAsState(initial = initialCoinType).value
        )

        var currentRoute by remember { mutableStateOf(NavRoute.Home) }
        LaunchedEffect(currentRoute) {
            navController.navigate(currentRoute.name)
        }

        Scaffold(
            topBar = {
                // to run the animation independently
                val coroutineScope = rememberCoroutineScope()
                CenterAlignedTopAppBar(
                    title = { Title(currentRoute) },
                    navigationIcon = {
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.open() } }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = "MenuButton"
                            )
                        }
                    },
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        scrolledContainerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                val coroutineScope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(
                            onClick = { selectedRoute ->
                                coroutineScope.launch {
                                    if (currentRoute == selectedRoute) {
                                        drawerState.close()
                                    } else {
                                        currentRoute = selectedRoute
                                        drawerState.close()
                                    }
                                }

                            }
                        )
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = MAIN_ROUTE,
                        enterTransition = { fadeIn(tween(NAV_TRANSITION_DURATION)) },
                        exitTransition = { fadeOut(tween(NAV_TRANSITION_DURATION)) }
                    ) {
                        mainGraph()
                    }
                }
            }
        }
    }

    @Composable
    private fun Title(currentRoute: NavRoute) {
        val title = stringResource(
            id = when (currentRoute) {
                NavRoute.Home -> R.string.empty_string
                NavRoute.CoinList -> R.string.choose_a_coin
                NavRoute.Settings -> R.string.settings
            }
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
